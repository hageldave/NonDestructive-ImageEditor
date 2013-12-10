package manipulators.image;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.AdjustCompleteChangeListener;
import manipulators.pixel.TransparencyManipulation;
import model.PixelArray;

public class ImageLayer extends ImageManipulation {

	PixelArray image;
	PixelArray imageFast;
	TransparencyManipulation transparency = new TransparencyManipulation();
	
	public ImageLayer(PixelArray image) {
		this.image = image;
	}
	
	public int getTransparencyplus() {
		return transparency.getTransparencyplus();
	}

	public void setTransparency(int transparencyplus) {
		int oldval = getTransparencyplus();
		transparency.setTransparencyplus(transparencyplus);
		firePropertyChange(new PropertyChangeEvent(this, "image_transparency", oldval, transparencyplus));
	}

	/** does no manipulation at all, just returns the argument.
	 * Use {@link #getLayerFast(int)} or {@link #getLayer()}
	 */
	@Override
	@Deprecated
	public PixelArray manipulatePixels(PixelArray image) {
		return image;
	}
	
	
	/** does no manipulation at all, just returns the argument.
	 * Use {@link #getLayerFast(int)} or {@link #getLayer()}
	 */
	@Override
	@Deprecated
	public PixelArray manipulatePixelsFast(PixelArray image) {
		return image;
	}
	
	protected PixelArray applyTransparency(PixelArray pixArr){
		for(int i = 0; i < pixArr.getNumOfPixels(); i++){
			pixArr.setRGBAatIndex(i, transparency.manipulatePixel(pixArr.getRGBAatIndex(i)));
		}
		return pixArr;
	}
	
	
	public PixelArray getLayer(){
		return applyTransparency(new PixelArray(this.image));
	}
	
	public PixelArray getLayerFast(int maxPixels){
		int width = image.getWidth();
		int height = image.getHeight();
		int div = 1;
		while(width*height > maxPixels && width > 2 && height > 2){
			div *= 2;
			width /= div;
			height /= div;
		}
		if(imageFast != null && imageFast.getWidth() == width && imageFast.getHeight() == height){
			return applyTransparency(imageFast);
		} else {
			imageFast = new PixelArray(this.image, div);
			return applyTransparency(imageFast);
		}
	}

	@Override
	protected JComponent makeGUIEditor() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setName("Imagelayer transparency");
		final JSlider transparency = new JSlider(-256, 256, 0);
		transparency.addChangeListener(new AdjustCompleteChangeListener() {
			@Override
			public void changed(ChangeEvent e) {
				setTransparency(transparency.getValue());
			}
		});
		panel.add(new JTextField("layer#"+image.toString()), BorderLayout.SOUTH);
		panel.add(transparency, BorderLayout.CENTER);
		return panel;
	}

	@Override
	public String getManipulationName() {
		return "ImageLayer";
	}

}
