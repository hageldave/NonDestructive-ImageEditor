package manipulators.pixel;

public class ColorMixManipulation extends PixelManipulation {

	int rplus=0;
	int gplus=0;
	int bplus=0;
	
	@Override
	protected int[] manipulate(int[] rgba) {
		rgba[0] +=rplus;
		rgba[1] +=gplus;
		rgba[2] +=bplus;
		return rgba;
	}

	public int getRplus() {
		return rplus;
	}

	public void setRplus(int rplus) {
		this.rplus = rplus;
	}

	public int getGplus() {
		return gplus;
	}

	public void setGplus(int gplus) {
		this.gplus = gplus;
	}

	public int getBplus() {
		return bplus;
	}

	public void setBplus(int bplus) {
		this.bplus = bplus;
	}

}
