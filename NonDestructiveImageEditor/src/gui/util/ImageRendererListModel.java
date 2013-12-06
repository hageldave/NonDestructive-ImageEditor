package gui.util;


import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import manipulators.image.ImageManipulation;
import model.ImageRenderer;

public class ImageRendererListModel implements ListModel<ImageManipulation>, Observer {
	
	ImageRenderer renderer;
	LinkedList<ListDataListener> listeners = new LinkedList<>();
	
	public ImageRendererListModel(ImageRenderer renderer) {
		this.renderer = renderer;
		renderer.addObserver(this);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}

	@Override
	public ImageManipulation getElementAt(int index) {
		return renderer.getManipulation(index);
	}

	@Override
	public int getSize() {
		return renderer.getNumOfManipulation();
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}

	
	public boolean removeElementAt(int index){
		return renderer.removeManipulation(index) != null;
	}

	public boolean liftElement(int elementIndex, int levels) {
		return renderer.liftElement(elementIndex, levels);
	}

	@Override
	public void update(Observable o, Object arg) {
		if(arg instanceof Integer){
			ListDataEvent ev = new ListDataEvent(this,(int)arg, 0, getSize());
			for(ListDataListener l: listeners){
				l.contentsChanged(ev);
			}
		}
	}
	
}
