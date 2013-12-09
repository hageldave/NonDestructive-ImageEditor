package manipulators.image;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.AdjustCompleteChangeListener;
import manipulators.pixel.ContrastManipulation;

public class ImageContrastManipulation extends ImageColorManipulation {
	
	ContrastManipulation contrast = new ContrastManipulation();
	
	public ImageContrastManipulation() {
//		contrast.setOutOfBoundFixEnabled(true);
	}

	@Override
	public int[] changePixelColor(int[] rgb) {
		return contrast.manipulatePixel(rgb);
	}

	public int getThreshold() {
		return contrast.getThreshold();
	}

	public void setThreshold(int threshold) {
		int oldthreshold = contrast.getThreshold();
		contrast.setThreshold(threshold);
		firePropertyChange(new PropertyChangeEvent(this, "contrast_threshold", oldthreshold, threshold));
	}

	public float getIntensity() {
		return contrast.getIntensity();
	}

	public void setIntensity(float intensity) {
		float oldintensity = contrast.getIntensity();
		contrast.setIntensity(intensity);
		firePropertyChange(new PropertyChangeEvent(this, "contrast_intensity", oldintensity, intensity));
	}
	
	
	protected JComponent makeGUIEditor(){
		JPanel panel = new JPanel(new BorderLayout());
		panel.setName("Contrast");
		JLabel label = new JLabel("Contrast");
		final JSlider intensityslider = new JSlider(-128, 256, 0);
		final JSlider thresholdslider = new JSlider(-1, 256, 127);
		panel.add(label, BorderLayout.NORTH);
		panel.add(thresholdslider, BorderLayout.SOUTH);
		panel.add(intensityslider, BorderLayout.CENTER);
		intensityslider.addChangeListener(new AdjustCompleteChangeListener() {
			@Override
			public void changed(ChangeEvent e) {
				setIntensity(intensityslider.getValue()/128f);
			}
		});
		thresholdslider.addChangeListener(new AdjustCompleteChangeListener() {
			@Override
			public void changed(ChangeEvent e) {
				setThreshold(thresholdslider.getValue());
			}
		});
		return panel;
	}

}
