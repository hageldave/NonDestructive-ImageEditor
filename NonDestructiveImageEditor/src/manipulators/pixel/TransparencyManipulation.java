package manipulators.pixel;

public class TransparencyManipulation extends PixelManipulation {

	int transparencyplus = 0;
	
	@Override
	protected int[] manipulate(int[] rgba) {
		rgba[3] = rgba[3] + transparencyplus;
		return rgba;
	}

	public int getTransparencyplus() {
		return transparencyplus;
	}

	public void setTransparencyplus(int transparencyplus) {
		this.transparencyplus = transparencyplus;
	}

}
