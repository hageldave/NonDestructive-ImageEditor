package manipulators.pixel;

public class SaturationManipulation extends PixelManipulation {

	float saturation;
	
	@Override
	public int[] manipulate(int[] rgba) {
		return saturate(rgba, saturation);
	}

	
	int[] saturate(int[] rgba, float saturation){
		int r = rgba[0];
		int g = rgba[1];
		int b = rgba[2];
		
		int grey = (r+g+b)/3; // average
		
		r = (int)(r + ((r - grey)*saturation));
		g = (int)(g + ((g - grey)*saturation));
		b = (int)(b + ((b - grey)*saturation));
		
		rgba[0] = r;
		rgba[1] = g;
		rgba[2] = b;
		return rgba;
	}


	public float getSaturation() {
		return saturation;
	}


	public void setSaturation(float saturation) {
		this.saturation = saturation;
	}

}
