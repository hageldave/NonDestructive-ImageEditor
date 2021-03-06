package model;

import java.awt.image.BufferedImage;

public class PixelArray implements IPixelCollection {
	private long[] rgbaPixels = new long[0];
	private int imageWidth = 0;
	private int divider = 1;
	
	protected static final int minNegative16 = -21845; // = -(2^16)/3
	protected static final int maxPositive16 = 43689; // = 2*(2^16)/3 -1 or 2^16 + minNegative16 -1
	
	/* (non-Javadoc)
	 * @see model.IPixelCollection#getRGBA(int, int)
	 */
	@Override
	public int[] getRGBA(int x, int y){
		return separateRGBA16(rgbaPixels[y*imageWidth + x]);
	}
	
	
	/* (non-Javadoc)
	 * @see model.IPixelCollection#getRGBAatIndex(int)
	 */
	@Override
	public int[] getRGBAatIndex(int index){
		return separateRGBA16(rgbaPixels[index]);
	}
	
	/* (non-Javadoc)
	 * @see model.IPixelCollection#setRGBA(int, int, int[])
	 */
	@Override
	public void setRGBA(int x, int y, int[] rgba){
		rgbaPixels[y*imageWidth + x] = recombineRGBA16(rgba);
	}
	
	/* (non-Javadoc)
	 * @see model.IPixelCollection#setRGBAatIndex(int, int[])
	 */
	@Override
	public void setRGBAatIndex(int index, int[] rgba){
		rgbaPixels[index] = recombineRGBA16(rgba);
	}
	
	
	protected void setRGBA16(int x, int y, long rgba16){
		rgbaPixels[y*imageWidth + x] = rgba16;
	}
	
	/* (non-Javadoc)
	 * @see model.IPixelCollection#getHeight()
	 */
	@Override
	public int getHeight() {
		return rgbaPixels.length/imageWidth;
	}
	
	/* (non-Javadoc)
	 * @see model.IPixelCollection#getWidth()
	 */
	@Override
	public int getWidth() {
		return imageWidth;
	}
	
	/* (non-Javadoc)
	 * @see model.IPixelCollection#getNumOfPixels()
	 */
	@Override
	public int getNumOfPixels(){
		return rgbaPixels.length;
	}
	
	public int getDivider() {
		return divider;
	}
	
	
	public PixelArray(int width, int height) {
		this.imageWidth = width;
		this.rgbaPixels = new long[width*height];
	}

	public PixelArray(long[] rgbaPixels, int imageWidth) {
		this.rgbaPixels = rgbaPixels;
		if(imageWidth < 0){
			throw new RuntimeException("PixelArray cannot have negative width: " + imageWidth);
		} else {
			this.imageWidth = imageWidth;
		}
	}
	
	public PixelArray(PixelArray pixArr) {
		this.imageWidth = pixArr.imageWidth;
		this.rgbaPixels = new long[pixArr.getNumOfPixels()];
		for(int i = 0; i < pixArr.rgbaPixels.length; i++){
			// copy values (dont copy array!)
			rgbaPixels[i] = pixArr.rgbaPixels[i];
		}
	}
	
	public PixelArray(IPixelCollection pixArr, int divider) {
		this.divider = divider;
		imageWidth = pixArr.getWidth()/divider;
		rgbaPixels = new long[(pixArr.getWidth()/divider)*(pixArr.getHeight()/divider)];
		for(int y = 0; y < pixArr.getHeight()/divider; y++){
			for(int x = 0; x < pixArr.getWidth()/divider; x++){
				int[] rgba = pixArr.getRGBA(x*divider, y*divider);
				setRGBA16(x, y, recombineRGBA16(rgba));
			}
		}
	}
	
	public PixelArray(BufferedImage img){
		imageWidth = img.getWidth();
		rgbaPixels = new long[img.getWidth()*img.getHeight()];
		for(int y = 0; y < img.getHeight(); y++){
			for(int x = 0; x < img.getWidth(); x++){
				setRGBA16(x, y, recombineRGBA16(separateRGBA(img.getRGB(x, y))));
			}
		}
	}
	
	public PixelArray(BufferedImage img, int divider){
		this.divider = divider;
		imageWidth = img.getWidth()/divider;
		rgbaPixels = new long[(img.getWidth()/divider)*(img.getHeight()/divider)];
		for(int y = 0; y < img.getHeight()/divider; y++){
			for(int x = 0; x < img.getWidth()/divider; x++){
				setRGBA16(x, y, recombineRGBA16(separateRGBA(img.getRGB(x*divider, y*divider))));
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see model.IPixelCollection#generateBufferedImage()
	 */
	@Override
	public BufferedImage generateBufferedImage(){
		int[] rgbaCompoundArray = new int[this.getNumOfPixels()];
		for(int i = 0; i < getNumOfPixels(); i++){
			rgbaCompoundArray[i] = recombineRGBA(fixOutOfBounds(this.getRGBAatIndex(i)));
		}
		BufferedImage img = new BufferedImage(imageWidth, rgbaPixels.length/imageWidth, BufferedImage.TYPE_4BYTE_ABGR);
		img.setRGB(0, 0, imageWidth, getHeight(), rgbaCompoundArray, 0, imageWidth);
		return img;
	}
	
	protected static int[] separateRGBA(int rgba){
		int[] rgbaArray = new int[4];
		rgbaArray[3] = (rgba >> 24) & 0xFF;
		rgbaArray[0] = (rgba >> 16) & 0xFF;
		rgbaArray[1] = (rgba >> 8) & 0xFF;
		rgbaArray[2] = rgba & 0xFF;
		return rgbaArray;
	}
	
	protected static int recombineRGBA(int[] rgba){
		int rgbCompound = rgba[3]; // alpha
		rgbCompound = (rgbCompound << 8) +rgba[0];
		rgbCompound = (rgbCompound << 8) +rgba[1];
		rgbCompound = (rgbCompound << 8) +rgba[2];
		return rgbCompound;
	}
	
	public static int[] fixOutOfBounds(int[] rgba){
		rgba[0] = rgba[0]>0 ? (rgba[0]<255 ? rgba[0]:255):0;
		rgba[1] = rgba[1]>0 ? (rgba[1]<255 ? rgba[1]:255):0;
		rgba[2] = rgba[2]>0 ? (rgba[2]<255 ? rgba[2]:255):0;
		rgba[3] = rgba[3]>0 ? (rgba[3]<255 ? rgba[3]:255):0;
		return rgba;
	}
	
	protected static int[] separateRGBA16(long rgba16){
		int[] rgbaArray = new int[4];
		rgbaArray[3] = (int)((rgba16 >> 48) & 0xFFFF)+minNegative16;
		rgbaArray[0] = (int)((rgba16 >> 32) & 0xFFFF)+minNegative16;
		rgbaArray[1] = (int)((rgba16 >> 16) & 0xFFFF)+minNegative16;
		rgbaArray[2] = (int)((rgba16 & 0xFFFF))+minNegative16;
		return rgbaArray;
	}
	
	protected static long recombineRGBA16(int[] rgba){
		long rgbCompound = rgba[3]-minNegative16; // alpha channel
		rgbCompound = (rgbCompound << 16) +rgba[0]-minNegative16; // minNegative16 is used for allowance of negative rgba values
		rgbCompound = (rgbCompound << 16) +rgba[1]-minNegative16;
		rgbCompound = (rgbCompound << 16) +rgba[2]-minNegative16;
		return rgbCompound;
	}
	
	public static int[] fixOutOfBounds16(int[] rgba16){
		rgba16[0] = rgba16[0]>minNegative16 ? (rgba16[0]<maxPositive16 ? rgba16[0]:maxPositive16) :minNegative16;
		rgba16[1] = rgba16[1]>minNegative16 ? (rgba16[1]<maxPositive16 ? rgba16[1]:maxPositive16) :minNegative16;
		rgba16[2] = rgba16[2]>minNegative16 ? (rgba16[2]<maxPositive16 ? rgba16[2]:maxPositive16) :minNegative16;
		rgba16[3] = rgba16[3]>minNegative16 ? (rgba16[3]<maxPositive16 ? rgba16[3]:maxPositive16) :minNegative16;
		return rgba16;
	}
	
	
}