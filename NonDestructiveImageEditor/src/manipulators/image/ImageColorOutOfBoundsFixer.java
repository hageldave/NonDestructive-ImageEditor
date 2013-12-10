package manipulators.image;

import javax.swing.JComponent;
import javax.swing.JPanel;

import manipulators.pixel.PixelManipulation;

public class ImageColorOutOfBoundsFixer extends ImageColorManipulation {
	
	PixelManipulation fixer = new PixelManipulation() {
		
		@Override
		public int[] manipulate(int[] rgba) {
			return rgba;
		}
	};

	public ImageColorOutOfBoundsFixer() {
		fixer.setOutOfBoundFixEnabled(true);
	}
	
	@Override
	public int[] changePixelColor(int[] rgba) {
		return fixer.manipulatePixel(rgba);
	}

	@Override
	protected JComponent makeGUIEditor() {
		JPanel panel = new JPanel();
		panel.setName("colorfix");
		return panel;
	}

	@Override
	public String getManipulationName() {
		return "Color Out Of Bounds Fix";
	}

}
