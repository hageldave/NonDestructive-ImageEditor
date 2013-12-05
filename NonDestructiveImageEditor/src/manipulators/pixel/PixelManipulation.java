package manipulators.pixel;

public abstract class PixelManipulation {
	
	boolean outOfBoundFixEnabled = false;
	
	
	public int[] manipulatePixel(int[] rgba) {
		return outOfBoundFixEnabled ? fixOutOfBounds(manipulate(rgba)):manipulate(rgba);
	}
	
	public int[] manipulatePixelFast(int[] rgba){
		return outOfBoundFixEnabled ? fixOutOfBounds(manipulateFast(rgba)):manipulateFast(rgba);
	}

	protected abstract int[] manipulate(int[] rgba);
	
	protected int[] manipulateFast(int[] rgba) {
		return manipulate(rgba);
	}
	
	public int[] separateRGBA(int rgba){
		int[] rgbaArray = new int[4];
		rgbaArray[0] = (rgba >> 16) & 0xFF;
		rgbaArray[1] = (rgba >> 8) & 0xFF;
		rgbaArray[2] = rgba & 0xFF;
		rgbaArray[3] = (rgba >> 24) & 0xFF;
		return rgbaArray;
	}
	
	public int recombineRGBA(int[] rgba){
		int rgbCompound = rgba[3]; // alpha
		rgbCompound = (rgbCompound << 8) +rgba[0];
		rgbCompound = (rgbCompound << 8) +rgba[1];
		rgbCompound = (rgbCompound << 8) +rgba[2];
		return rgbCompound;
	}
	
	protected int[] fixOutOfBounds(int[] rgba){
		rgba[0] = rgba[0]>0 ? (rgba[0]<255 ? rgba[0]:255):0;
		rgba[1] = rgba[1]>0 ? (rgba[1]<255 ? rgba[1]:255):0;
		rgba[2] = rgba[2]>0 ? (rgba[2]<255 ? rgba[2]:255):0;
		rgba[3] = rgba[3]>0 ? (rgba[3]<255 ? rgba[3]:255):0;
		return rgba;
	}
	
	public void setOutOfBoundFixEnabled(boolean enable){
		this.outOfBoundFixEnabled = enable;
	}
}