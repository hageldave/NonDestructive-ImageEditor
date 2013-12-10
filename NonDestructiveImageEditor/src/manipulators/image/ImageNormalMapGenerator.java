package manipulators.image;

import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import manipulators.pixel.PixelManipulation;
import model.PixelArray;

public class ImageNormalMapGenerator extends ImageManipulation {

	PixelManipulation outofboundsfix = new PixelManipulation() {
		
		@Override
		protected int[] manipulate(int[] rgba) {
			rgba = fixOutOfBounds(rgba);
			return rgba;
		}
	};
	
	private int r = 1;
	
	public void setRadius(int rad){
		int oldval = r;
		this.r = rad;
		firePropertyChange(new PropertyChangeEvent(this, "normalmap_radius", oldval, rad));
	}
	
	public int getRadius(){
		return r;
	}

	
	private float getGreyF(int[] rgba){
		return (0f+rgba[0]+rgba[1]+rgba[2])/3f; //avg
	}
	@Override
	public PixelArray manipulatePixelsFast(PixelArray image) {
		
		for(int i = 0; i < image.getNumOfPixels(); i++){
			image.setRGBAatIndex(i, outofboundsfix.manipulatePixel(image.getRGBAatIndex(i)));
		}

		PixelArray normalmap = new PixelArray(image.getWidth(), image.getHeight());
		
		int width = image.getWidth();
		int height = image.getHeight();
		
//		float vec1_0 = 0;
//		float vec1_1 = 0;
		float vec1_2 = 0;
//		float vec2_0 = 0;
		float vec2_1 = 0;
		float vec2_2 = 0;
		
		for(int x = 0; x < width; x++){

			for(int y = 0; y < height; y++){
				if(x+1 < width){
//					vec1_0 =1; 
//					vec1_1 =0;
					vec1_2=getGreyF(image.getRGBA(x+1, y)) - getGreyF(image.getRGBA(x, y));
				} else {
//					vec1_0 =1; 
//					vec1_1 =0;
					vec1_2= 0; // fake pixel bei x+1 mit selbem wert
				}
				
				if(y+r < height){
//					vec2_0 =0; 
//					vec2_1 =1;
					vec2_2=getGreyF(image.getRGBA(x, y+1)) - getGreyF(image.getRGBA(x, y));
				} else {
//					vec2_0 =0; 
//					vec2_1 =1;
					vec2_2= 0; // fake pixel bei y+1 mit selbem wert
				}
				
//				Eigentliches kreuzprodukt
//				(vec1_1*vec2_2 - vec1_2*vec2_1),
//				(vec1_2*vec2_0 - vec1_0*vec2_2),
//				(vec1_0*vec2_1 - vec1_1*vec2_0)
//				float len = (float) Math.sqrt(normal[0]*normal[0]+normal[1]*normal[1]+normal[2]*normal[2]);
//				normal[0]/=len;
//				normal[1]/=len;
//				normal[2]/=len;
				
				//optimiertes kreuzprodukt
				vec1_2 = -vec1_2;
				vec2_2 = -vec2_2;
				vec2_1 =  1;
				// einheitsvektor
//				float len = (float) Math.sqrt(vec1_2*vec1_2+vec2_2*vec2_2+vec2_1*vec2_1);
				// optimiert
				float len = (float) Math.sqrt(vec1_2*vec1_2+vec2_2*vec2_2+1);
				vec1_2 /=len;
				vec2_2 /=len;
				vec2_1 /=len;
				
				int[] normalcolors = new int[]{
//						(int)((normal[0]*255f)/2f +128f),
//						(int)((normal[1]*255f)/2f +128f),
//						(int)((normal[2]*255f)/2f +128f),
						(int)(vec1_2*127.5f +128f),
						(int)(vec2_2*127.5f +128f),
						(int)(vec2_1*127.5f +128f),
						image.getRGBA(x, y)[3]};
				normalmap.setRGBA(x, y, normalcolors);
			}
		}

		return normalmap;
	}
	
	
	@Override
	public PixelArray manipulatePixels(PixelArray image) {
		return manipulatePixelsFast(image);
	}
		
	
	
	@Override
	protected JComponent makeGUIEditor() {
		JPanel panel = new JPanel();
		panel.setName("normalmap");
		final JSlider radius = new JSlider(1, 20, 1);
		panel.add(radius);
		radius.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				setRadius(radius.getValue());
			}
		});
		return panel;
	}

	@Override
	public String getManipulationName() {
		return "To NormalMap";
	}
}
