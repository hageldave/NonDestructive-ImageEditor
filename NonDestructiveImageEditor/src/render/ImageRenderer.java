package render;

import gui.StatusBar;

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import util.ObservableArrayList;
import manipulators.image.ImageLayer;
import manipulators.image.ImageManipulation;
import manipulators.image.ImageMerger;
import model.PixelArray;

public class ImageRenderer extends Observable implements Observer, PropertyChangeListener {

	private ObservableArrayList<ImageManipulation> manipulationlist = new ObservableArrayList<>();

	private int maxNumPreviewPx = 100000; // aprox 316x316px
	
	private RenderJob currentJob;
	
	private LinkedList<RenderJob> renderJobQueue = new LinkedList<>();
	
	public ImageRenderer() {
		manipulationlist.addObserver(this);
	}
	
	/** gets called when manipulationlist changes */
	@Override
	public void update(Observable o, Object arg) {
		// pass update event
		this.setChanged();
		this.notifyObservers(arg);
	}

	/** gets called when element in manipulationlist changes */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		this.setChanged();
		this.notifyObservers(evt);
	}

	public int getNumOfManipulation() {
		return manipulationlist.size();
	}

	public boolean addManipulation(ImageManipulation e) {
		boolean success = manipulationlist.add(e);
		if(success){
			e.addPropertyChangeListener(this);
		}
		return success;
	}

	public void addManipulation(int index, ImageManipulation element) {
		if(element != null){
			manipulationlist.add(index, element);
			element.addPropertyChangeListener(this);
		}
	}
	
	
	public boolean liftElement(int elementIndex, int levels){
		return manipulationlist.setElementToIndex(elementIndex, elementIndex-levels);
	}

	public ImageManipulation removeManipulation(int index) {
		ImageManipulation removed = manipulationlist.remove(index);
		if(!manipulationlist.contains(removed)){
			// no duplicate of elem in list
			removed.removePropertyChangeListener(this);
		}
		return removed;
	}

	public boolean removeManipulation(ImageManipulation element) {
		boolean success = manipulationlist.remove(element);
		if(!manipulationlist.contains(element)){
			// no duplicate of elem in list
			element.removePropertyChangeListener(this);
		}
		return success;
	}

	public ImageManipulation getManipulation(int index) {
		return manipulationlist.get(index);
	}
	
	
	// ------------------------------------------------------
	
	
	public void executeRendering(boolean isPreview, PostRenderAction afterwards){
		RenderJob job = new RenderJob(this, isPreview, afterwards);
		job.execute();
		if(currentJob != null && !currentJob.isDone()){
			if(currentJob.getPostRenderActionID().equals(afterwards.getId())){
				long time = System.currentTimeMillis();
				System.out.println("cancelling result: " + currentJob.cancel(true));
				System.out.println(System.currentTimeMillis() - time);
			}
		}
		currentJob = job;
		Thread.yield();
	}
	
	
	public BufferedImage renderPreview(){
		StatusBar.getInstance().setCurrentStatus("Rendering ");
		StatusBar.getInstance().setCurrentProgress(0);
		
		LinkedList<PixelArray> renderedLayers = getRenderedLayers(getLayerTasks(), true);
		if(renderedLayers.isEmpty()){
			return null;
		}
		try {
			BufferedImage toReturn = mergeRenderedLayers(renderedLayers).generateBufferedImage();
			return toReturn;
		} finally {
			StatusBar.getInstance().setCurrentStatus("");
			StatusBar.getInstance().setCurrentProgress(0);
			System.gc();
		}
	}
	
	
	public BufferedImage renderFull(){
		StatusBar.getInstance().setCurrentStatus("Rendering ");
		StatusBar.getInstance().setCurrentProgress(0);
		
		LinkedList<PixelArray> renderedLayers = getRenderedLayers(getLayerTasks(), false);
		if(renderedLayers.isEmpty()){
			return null;
		}
		try {
			BufferedImage toReturn = mergeRenderedLayers(renderedLayers).generateBufferedImage();
			return toReturn;
		} finally {
			StatusBar.getInstance().setCurrentStatus("");
			StatusBar.getInstance().setCurrentProgress(0);
			System.gc();
		}
	}
	
	
	private PixelArray mergeRenderedLayers(LinkedList<PixelArray> layers){
		ImageMerger merger = ImageMerger.getInstance();
		if(layers.size() < 1){
			return null;
		} else if(layers.size() == 1){
			return layers.getFirst();
		} else {
			PixelArray bottom = layers.pollFirst();
			while(!layers.isEmpty()){
				bottom = merger.merge(bottom, layers.pollFirst());
			}
			return bottom;
		}
	}
	
	
	private LinkedList<LinkedList<ImageManipulation>> getLayerTasks(){
		LinkedList<LinkedList<ImageManipulation>> tasks = new LinkedList<>();
		LinkedList<ImageManipulation> layertask = new LinkedList<>();
		for(ImageManipulation im: manipulationlist ){
			if(im instanceof ImageLayer){
				layertask = new LinkedList<>();
				tasks.add(layertask);
				layertask.add(im); // disabled layers are filtered out later 
			} else {
				if(im.isEnabled()){
					layertask.add(im);
				}
			}
		}
		LinkedList<LinkedList<ImageManipulation>> toRemove = new LinkedList<>();
		for(LinkedList<ImageManipulation> task: tasks){
			if(!((ImageLayer)task.getFirst()).isEnabled()){
				toRemove.add(task);
			}
		}
		tasks.removeAll(toRemove);
		
		return tasks;
	}
	
	private LinkedList<PixelArray> getRenderedLayers(LinkedList<LinkedList<ImageManipulation>> layertasklist, boolean fast){
		LinkedList<PixelArray> renderedlayers = new LinkedList<>();
		int i = 0; int num = layertasklist.size();
		for(LinkedList<ImageManipulation> layertask: layertasklist){
			ImageLayer layer = (ImageLayer) layertask.pollFirst();
			
			i++;
			StatusBar.getInstance().setCurrentStatus("Rendereing Layer "+i+" of "+num);
			
			renderedlayers.add(renderLayer(layer, layertask, fast));
		}
		return renderedlayers;
	}
	
	private PixelArray renderLayer(ImageLayer layer, LinkedList<ImageManipulation> manips, boolean fast){
		int i = 0; float num = manips.size();
		if(fast){
			PixelArray pa = layer.getLayerFast(maxNumPreviewPx);
			for(ImageManipulation im: manips){
				pa = im.manipulatePixelsFast(pa);
				i++;
				StatusBar.getInstance().setCurrentProgress((int)((i/num)*100));
			}
			System.gc();
			return pa;
		} else {
			PixelArray pa = layer.getLayer();
			for(ImageManipulation im: manips){
				pa = im.manipulatePixels(pa);
				i++;
				StatusBar.getInstance().setCurrentProgress((int)((i/num)*100));
			}
			System.gc();
			return pa;
		}
	}
	
}
