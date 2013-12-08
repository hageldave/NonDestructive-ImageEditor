package model;

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import util.ObservableArrayList;
import manipulators.image.ImageLayer;
import manipulators.image.ImageManipulation;

public class ImageRenderer extends Observable implements Observer, PropertyChangeListener {

	private ObservableArrayList<ImageManipulation> manipulationlist = new ObservableArrayList<>();

	private int maxNumPreviewPx = 100000; // aprox 316x316px
	
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
	
	public BufferedImage renderPreview(){
		LinkedList<PixelArray> renderedLayers = getRenderedLayers(getLayerTasks(), true);
		if(renderedLayers.isEmpty()){
			return null;
		}
		// TODO:merge layers
		try {
			return renderedLayers.getFirst().generateBufferedImage();// placeholder
		} finally {
			System.gc();
		}
	}
	
	
	public BufferedImage renderFull(){
		LinkedList<PixelArray> renderedLayers = getRenderedLayers(getLayerTasks(), false);
		if(renderedLayers.isEmpty()){
			return null;
		}
		// TODO:merge layers
		try {
			return renderedLayers.getFirst().generateBufferedImage();// placeholder
		} finally {
			System.gc();
		}
	}
	
	
	private LinkedList<LinkedList<ImageManipulation>> getLayerTasks(){
		LinkedList<LinkedList<ImageManipulation>> tasks = new LinkedList<>();
		LinkedList<ImageManipulation> layertask = new LinkedList<>();
		for(ImageManipulation im: manipulationlist ){
			if(im instanceof ImageLayer){
				layertask = new LinkedList<>();
				tasks.add(layertask);
				layertask.add(im); // layers are filtered out later in getRenderedLayers
			} else {
				if(im.isEnabled()){
					layertask.add(im);
				}
			}
		}
		return tasks;
	}
	
	private LinkedList<PixelArray> getRenderedLayers(LinkedList<LinkedList<ImageManipulation>> layertasklist, boolean fast){
		LinkedList<PixelArray> renderedlayers = new LinkedList<>();
		for(LinkedList<ImageManipulation> layertask: layertasklist){
			ImageLayer layer = (ImageLayer) layertask.pollFirst();
			if(layer.isEnabled()){
				renderedlayers.add(renderLayer(layer, layertask, fast));
			}
		}
		return renderedlayers;
	}
	
	private PixelArray renderLayer(ImageLayer layer, LinkedList<ImageManipulation> manips, boolean fast){
		if(fast){
			PixelArray pa = layer.getLayerFast(maxNumPreviewPx);
			for(ImageManipulation im: manips){
				pa = im.manipulatePixelsFast(pa);
			}
			return pa;
		} else {
			PixelArray pa = layer.getLayer();
			for(ImageManipulation im: manips){
				pa = im.manipulatePixels(pa);
			}
			return pa;
		}
	}
	
	public PixelArray renderPixels(PixelArray pixels){
		for(ImageManipulation im: manipulationlist){
			pixels = im.manipulatePixels(pixels);
		}
		return pixels;
	}
	
	public PixelArray renderPixelsForPreview(PixelArray pixels){
		for(ImageManipulation im: manipulationlist){
			pixels = im.manipulatePixelsFast(pixels);
		}
		return pixels;
	}
	
}
