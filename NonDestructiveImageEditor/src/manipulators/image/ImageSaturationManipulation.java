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
import manipulators.pixel.SaturationManipulation;

public class ImageSaturationManipulation extends ImageColorManipulation {
	
	SaturationManipulation saturation = new SaturationManipulation();
	
	public ImageSaturationManipulation() {
//		saturation.setOutOfBoundFixEnabled(true);
	}

	@Override
	public int[] changePixelColor(int[] rgb) {
		return saturation.manipulatePixel(rgb);
	}

	public float getSaturation() {
		return saturation.getSaturation();
	}

	public void setSaturation(float sat) {
		float oldsat = saturation.getSaturation();
		saturation.setSaturation(sat);
		firePropertyChange(new PropertyChangeEvent(this, "saturation_saturation", oldsat, sat));
	}
	
	
	protected JComponent makeGUIEditor(){
		final JPanel panel = new JPanel(new BorderLayout());
		panel.setName("Saturation");
		JLabel label = new JLabel("Saturation");
		final JSlider satSlider = new JSlider(-128, 256, 0);
		satSlider.setMajorTickSpacing(128);
		satSlider.setMinorTickSpacing(32);
		satSlider.setPaintTicks(true);
		panel.add(label, BorderLayout.NORTH);
		panel.add(satSlider, BorderLayout.CENTER);
		satSlider.addChangeListener(new AdjustCompleteChangeListener() {
			@Override
			public void changed(ChangeEvent e) {
				setSaturation(satSlider.getValue() / 128f);
			}
		});
		return panel;
	}

	
	@Override
	public String getManipulationName() {
		return "Saturation";
	}
}
