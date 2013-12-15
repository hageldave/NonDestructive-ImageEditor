package manipulators.image;

import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

import util.AdjustCompleteChangeListener;
import model.PixelArray;

public class ImageBlurManipulation extends ImageManipulation {

	private float relativeRadius = 0.1f;
	
	public float getRelativeRadius() {
		return relativeRadius;
	}

	public void setRelativeRadius(float relativeRadius) {
		float oldval = relativeRadius;
		this.relativeRadius = relativeRadius;
		firePropertyChange(new PropertyChangeEvent(this, "blur_radius", oldval, relativeRadius));
	}

	@Override
	public PixelArray manipulatePixels(PixelArray image) {
		PixelArray imgClone = new PixelArray(image); // TODO: solution without clone
		
		int redTotal = 0;
		int grnTotal = 0;
		int bluTotal = 0;
		int alpTotal = 0;
		
		int radius =(int) (image.getWidth()*relativeRadius)/2;
		// horizontal blur
		for(int y = 0; y < image.getHeight(); y++){
			redTotal = 0;
			bluTotal = 0;
			grnTotal = 0;
			alpTotal = 0;
			int numOfPxInAverage = 0;
			for(int i = 0; i < radius; i++){
				// setup lists
				if(i < image.getWidth()){
					int[] rgba = imgClone.getRGBA(i, y);
					redTotal += rgba[0];
					grnTotal += rgba[1];
					bluTotal += rgba[2];
					alpTotal += rgba[3];
					numOfPxInAverage++;
				}
			}
			for(int x = 0; x < image.getWidth(); x++){
				if(x > radius){
					// remove firsts from lists cause they're not in range of radius anymore
					int[] rgba = imgClone.getRGBA(x-radius-1, y);
					redTotal -= rgba[0];
					grnTotal -= rgba[1];
					bluTotal -= rgba[2];
					alpTotal -= rgba[3];
					numOfPxInAverage--;
				}
				if(x+radius < image.getWidth()){
					// add pixel that just got in range
					int[] rgba = imgClone.getRGBA(x+radius, y);
					redTotal += rgba[0];
					grnTotal += rgba[1];
					bluTotal += rgba[2];
					alpTotal += rgba[3];
					numOfPxInAverage++;
				}
				
				int[] rgba = image.getRGBA(x, y);
				rgba[0]=redTotal/numOfPxInAverage;
				rgba[1]=grnTotal/numOfPxInAverage;
				rgba[2]=bluTotal/numOfPxInAverage;
				rgba[3]=alpTotal/numOfPxInAverage;
			}
		}
		imgClone = new PixelArray(image);
		// vertical blur
		for (int x = 0; x < image.getWidth(); x++) {
			redTotal = 0;
			bluTotal = 0;
			grnTotal = 0;
			alpTotal = 0;
			int numOfPxInAverage = 0;
			for (int i = 0; i < radius; i++) {
				// setup lists
				if (i < image.getHeight()) {
					int[] rgba = imgClone.getRGBA(x, i);
					redTotal += rgba[0];
					grnTotal += rgba[1];
					bluTotal += rgba[2];
					alpTotal += rgba[3];
					numOfPxInAverage++;
				}
			}
			for (int y = 0; y < image.getHeight(); y++) {
				if (y > radius) {
					// remove firsts from lists cause they're not in range of
					// radius anymore
					int[] rgba = imgClone.getRGBA(x, y-radius-1);
					redTotal -= rgba[0];
					grnTotal -= rgba[1];
					bluTotal -= rgba[2];
					alpTotal -= rgba[3];
					numOfPxInAverage--;
				}
				if (y + radius < image.getHeight()) {
					// add pixel that just got in range
					int[] rgba = imgClone.getRGBA(x, y+radius);
					redTotal += rgba[0];
					grnTotal += rgba[1];
					bluTotal += rgba[2];
					alpTotal += rgba[3];
					numOfPxInAverage++;
				}

				int[] rgba = image.getRGBA(x, y);
				rgba[0]=redTotal/numOfPxInAverage;
				rgba[1]=grnTotal/numOfPxInAverage;
				rgba[2]=bluTotal/numOfPxInAverage;
				rgba[3]=alpTotal/numOfPxInAverage;
			}
		}
		

		return image;
	}

	@Override
	public String getManipulationName() {
		return "Blur";
	}

	@Override
	protected JComponent makeGUIEditor() {
		final JSlider slider = new JSlider(0, 256, (int)(getRelativeRadius()*256));
		slider.addChangeListener(new AdjustCompleteChangeListener() {
			
			@Override
			public void changed(ChangeEvent ev) {
				setRelativeRadius(slider.getValue()/256f);
			}
		});
		return slider;
	}

}
