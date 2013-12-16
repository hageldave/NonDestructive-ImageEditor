package manipulators.image;

import java.beans.PropertyChangeEvent;
import java.util.LinkedList;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

import util.AdjustCompleteChangeListener;
import model.PixelArray;

public class ImageBlurManipulation extends ImageManipulation {

	private float relativeRadius = 0.166f;
	
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
		
		int redTotal = 0;
		int grnTotal = 0;
		int bluTotal = 0;
		int alpTotal = 0;
		/* contains all color colors that are currently used in totals.
		 * When pixel comes out of range of radius the first 4 color values
		 * are popped and subtracted from the respective total.
		 */
		LinkedList<Integer> colorList = new LinkedList<>();
 		
		int radius =(int) (image.getWidth()*relativeRadius)/2;
		// horizontal blur
		for(int y = 0; y < image.getHeight(); y++){
			redTotal = 0;
			bluTotal = 0;
			grnTotal = 0;
			alpTotal = 0;
			int numOfPxInAverage = 0;
			colorList.clear();
			for(int i = 0; i < radius; i++){
				// setup totals for first pixel in row
				if(i < image.getWidth()){
					int[] rgba = image.getRGBA(i, y);
					redTotal += rgba[0]; colorList.add(rgba[0]);
					grnTotal += rgba[1]; colorList.add(rgba[1]);
					bluTotal += rgba[2]; colorList.add(rgba[2]);
					alpTotal += rgba[3]; colorList.add(rgba[3]);
					numOfPxInAverage++;
				}
			}
			for(int x = 0; x < image.getWidth(); x++){
				if(x > radius){
					// remove firsts from lists cause they're not in range of radius anymore
					redTotal -= colorList.pollFirst();
					grnTotal -= colorList.pollFirst();
					bluTotal -= colorList.pollFirst();
					alpTotal -= colorList.pollFirst();
					numOfPxInAverage--;
				}
				if(x+radius < image.getWidth()){
					// add pixel that just got in range
					int[] rgba = image.getRGBA(x+radius, y);
					redTotal += rgba[0]; colorList.add(rgba[0]);
					grnTotal += rgba[1]; colorList.add(rgba[1]);
					bluTotal += rgba[2]; colorList.add(rgba[2]);
					alpTotal += rgba[3]; colorList.add(rgba[3]);
					numOfPxInAverage++;
				}
				
				int[] rgba = image.getRGBA(x, y);
				rgba[0]=((rgba[0])+redTotal)/(numOfPxInAverage+1); // weight on center pixel
				rgba[1]=((rgba[1])+grnTotal)/(numOfPxInAverage+1);
				rgba[2]=((rgba[2])+bluTotal)/(numOfPxInAverage+1);
				rgba[3]=((rgba[3])+alpTotal)/(numOfPxInAverage+1);
				image.setRGBA(x, y, rgba);
			}
		}

		// vertical blur
		for (int x = 0; x < image.getWidth(); x++) {
			redTotal = 0;
			bluTotal = 0;
			grnTotal = 0;
			alpTotal = 0;
			int numOfPxInAverage = 0;
			colorList.clear();
			for (int i = 0; i < radius; i++) {
				// setup lists
				if (i < image.getHeight()) {
					int[] rgba = image.getRGBA(x, i);
					redTotal += rgba[0]; colorList.add(rgba[0]);
					grnTotal += rgba[1]; colorList.add(rgba[1]);
					bluTotal += rgba[2]; colorList.add(rgba[2]);
					alpTotal += rgba[3]; colorList.add(rgba[3]);
					numOfPxInAverage++;
				}
			}
			for (int y = 0; y < image.getHeight(); y++) {
				if (y > radius) {
					// remove firsts from lists cause they're not in range of
					// radius anymore
					redTotal -= colorList.pollFirst();
					grnTotal -= colorList.pollFirst();
					bluTotal -= colorList.pollFirst();
					alpTotal -= colorList.pollFirst();
					numOfPxInAverage--;
				}
				if (y + radius < image.getHeight()) {
					// add pixel that just got in range
					int[] rgba = image.getRGBA(x, y+radius);
					redTotal += rgba[0]; colorList.add(rgba[0]);
					grnTotal += rgba[1]; colorList.add(rgba[1]);
					bluTotal += rgba[2]; colorList.add(rgba[2]);
					alpTotal += rgba[3]; colorList.add(rgba[3]);
					numOfPxInAverage++;
				}

				int[] rgba = image.getRGBA(x, y);
				rgba[0]=((rgba[0])+redTotal)/(numOfPxInAverage+1); // weight on center pixel
				rgba[1]=((rgba[1])+grnTotal)/(numOfPxInAverage+1);
				rgba[2]=((rgba[2])+bluTotal)/(numOfPxInAverage+1);
				rgba[3]=((rgba[3])+alpTotal)/(numOfPxInAverage+1);
				image.setRGBA(x, y, rgba);
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
		final JSlider slider = new JSlider(0, 300, 100);
		slider.setMajorTickSpacing(100);
		slider.setMinorTickSpacing(20);
		slider.setPaintTicks(true);
		slider.addChangeListener(new AdjustCompleteChangeListener() {
			
			@Override
			public void changed(ChangeEvent ev) {
				int val = slider.getValue();
				if(val < 200){
					setRelativeRadius(val/(300f*(200f/val)));
				} else {
					setRelativeRadius(val/300f);
				}
			}
		});
		return slider;
	}

}
