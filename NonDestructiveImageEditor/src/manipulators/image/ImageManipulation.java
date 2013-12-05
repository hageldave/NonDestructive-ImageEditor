package manipulators.image;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;

import javax.swing.JComponent;

import model.PixelArray;

public abstract class ImageManipulation {
	
	private LinkedList<PropertyChangeListener> listeners = new LinkedList<>();
	private JComponent guiEditor;

	public abstract PixelArray manipulatePixels(PixelArray image);
	
	public PixelArray manipulatePixelsFast(PixelArray image){
		return manipulatePixels(image);
	}
	
	protected void notifyPropertyChange(PropertyChangeEvent ev) {
		for(PropertyChangeListener listener: listeners){
			listener.propertyChange(ev);
		}
	}
	
	public boolean addPropertyChangeListener(PropertyChangeListener listener) {
		if(!listeners.contains(listener)){
			return listeners.add(listener);
		} else {
			return false;
		}
	}

	public boolean removePropertyChangeListener(Object listener) {
		return listeners.remove(listener);
	}

	public JComponent getGUIEditor() {
		if(this.guiEditor == null){
			this.guiEditor = makeGUIEditor();
		}
		return guiEditor;
	}
	
	protected abstract JComponent makeGUIEditor();
}
