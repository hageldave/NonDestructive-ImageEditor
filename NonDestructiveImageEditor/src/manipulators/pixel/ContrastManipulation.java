package manipulators.pixel;


public class ContrastManipulation extends PixelManipulation {

	public int threshold = 128;
	public float intensity = 0f;
	
	
	@Override
	public int[] manipulate(int[] rgba) {
		return setcontrast(rgba, threshold, intensity);
	}
	
	private int[] setcontrast(int[] rgba, int threshold, float intensity){
		int r = rgba[0];
		int g = rgba[1];
		int b = rgba[2];
		
		int grey = (r+g+b)/3; // average
		float difference = grey - threshold;
		
		r = (int) (r + (difference*intensity));
		g = (int) (g + (difference*intensity));
		b = (int) (b + (difference*intensity));
		
		rgba[0] = r;
		rgba[1] = g;
		rgba[2] = b;
//		System.out.println(r + " " + g + " " + b);
		return rgba;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public float getIntensity() {
		return intensity;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}

}
