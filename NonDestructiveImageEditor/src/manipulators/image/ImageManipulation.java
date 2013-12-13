package manipulators.image;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
	
	public abstract String getManipulationName();
	
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
	
	
	private JPanel buildCompoundGuiEditor(JComponent guieditor){
		final JCheckBox enabler = new JCheckBox("on/off");
		enabler.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if(isEnabled() != enabler.isSelected()){
					ImageManipulation.this.setEnabled(enabler.isSelected());
				}
			}
		});
		enabler.setSelected(isEnabled());
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(enabler, BorderLayout.WEST);
		panel.add(guieditor, BorderLayout.EAST);
		return panel;
	}

	public JComponent getGUIEditor() {
		if(this.guiEditor == null){
			this.guiEditor = buildCompoundGuiEditor(makeGUIEditor());
		}
		return guiEditor;
	}
	
	protected abstract JComponent makeGUIEditor();
}
