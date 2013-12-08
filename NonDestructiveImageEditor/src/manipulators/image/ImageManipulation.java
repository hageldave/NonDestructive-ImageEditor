package manipulators.image;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;

import javax.swing.JComponent;

import model.PixelArray;

public abstract class ImageManipulation {
	
	private boolean enabled = true;
	private LinkedList<PropertyChangeListener> listeners = new LinkedList<>();
	private JComponent guiEditor;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		boolean oldVal = this.enabled;
		this.enabled = enabled;
		if(oldVal != enabled){
			firePropertyChange(new PropertyChangeEvent(this, enabled ? "enabled":"disabled", oldVal, enabled));
		}
	}

	public abstract PixelArray manipulatePixels(PixelArray image);
	
	public PixelArray manipulatePixelsFast(PixelArray image){
		return manipulatePixels(image);
	}
	
	protected void firePropertyChange(PropertyChangeEvent ev) {
		// only when manipulation is enabled or when its the disable event
		if(this.isEnabled() || ev.getPropertyName().equals("disabled")){
			for(PropertyChangeListener listener: listeners){
				listener.propertyChange(ev);
			}
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
