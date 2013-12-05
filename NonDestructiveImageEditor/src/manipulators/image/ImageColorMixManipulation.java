package manipulators.image;

import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import manipulators.pixel.ColorMixManipulation;

public class ImageColorMixManipulation extends ImageColorManipulation {

	ColorMixManipulation colormix = new ColorMixManipulation();
	
	
	
	public int getRplus() {
		return colormix.getRplus();
	}

	public void setRplus(int rplus) {
		int oldval = getRplus();
		colormix.setRplus(rplus);
		notifyPropertyChange(new PropertyChangeEvent(this, "colormix_red", oldval, rplus));
	}

	public int getGplus() {
		return colormix.getGplus();
	}

	public void setGplus(int gplus) {
		int oldval = getGplus();
		colormix.setGplus(gplus);
		notifyPropertyChange(new PropertyChangeEvent(this, "colormix_green", oldval, gplus));
	}

	public int getBplus() {
		return colormix.getBplus();
	}

	public void setBplus(int bplus) {
		int oldval = getBplus();
		colormix.setBplus(bplus);
		notifyPropertyChange(new PropertyChangeEvent(this, "colormix_blue", oldval, bplus));
	}

	@Override
	public int[] changePixelColor(int[] rgba) {
		return colormix.manipulatePixel(rgba);
	}

	@Override
	protected JComponent makeGUIEditor() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setName("Color Mixer");
		final JSlider red = new JSlider(-256,256,0);
		final JSlider green = new JSlider(-256,256,0);
		final JSlider blue = new JSlider(-256,256,0);
		red.setBackground(Color.red);
		green.setBackground(Color.green);
		blue.setBackground(Color.blue);
		red.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				setRplus(red.getValue());
			}
		});
		green.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				setGplus(green.getValue());
			}
		});
		blue.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				setBplus(blue.getValue());
			}
		});
		panel.add(red, BorderLayout.NORTH);
		panel.add(green, BorderLayout.CENTER);
		panel.add(blue, BorderLayout.SOUTH);
		return panel;
	}

}
