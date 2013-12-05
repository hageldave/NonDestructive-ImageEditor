package model;

import java.awt.image.BufferedImage;

public class PixelArray {
	private int[][] rgbaPixels = new int[0][4];
	private int imageWidth = 0;
	private int divider = 1;
	
	public int[] getRGBA(int x, int y){
		return rgbaPixels[y*imageWidth + x];
	}
	
	
	public int[] getRGBAatIndex(int index){
		return rgbaPixels[index];
	}
	
	public void setRGBA(int x, int y, int[] rgba){
		rgbaPixels[y*imageWidth + x] = rgba;
	}
	
	public void setRGBAatIndex(int index, int[] rgba){
		rgbaPixels[index] = rgba;
	}
	
	public int getHeight() {
		return rgbaPixels.length/imageWidth;
	}
	
	public int getWidth() {
		return imageWidth;
	}
	
	public int getNumOfPixels(){
		return rgbaPixels.length;
	}
	
	public int getDivider() {
		return divider;
	}
	
	
	public PixelArray(int width, int height) {
		this.imageWidth = width;
		this.rgbaPixels = new int[width*height][4];
	}

	public PixelArray(int[][] rgbaPixels, int imageWidth) {
		this.rgbaPixels = rgbaPixels;
		if(imageWidth < 0){
			throw new RuntimeException("PixelArray cannot have negative width: " + imageWidth);
		} else {
			this.imageWidth = imageWidth;
		}
	}
	
	public PixelArray(PixelArray pixArr) {
		this.imageWidth = pixArr.imageWidth;
		this.rgbaPixels = new int[pixArr.getNumOfPixels()][4];
		for(int i = 0; i < pixArr.rgbaPixels.length; i++){
			// copy values (dont copy rgba int[]!)
			rgbaPixels[i][0] = pixArr.rgbaPixels[i][0];
			rgbaPixels[i][1] = pixArr.rgbaPixels[i][1];
			rgbaPixels[i][2] = pixArr.rgbaPixels[i][2];
			rgbaPixels[i][3] = pixArr.rgbaPixels[i][3];
		}
	}
	
	public PixelArray(PixelArray pixArr, int divider) {
		this.divider = divider;
		imageWidth = pixArr.getWidth()/divider;
		rgbaPixels = new int[(pixArr.getWidth()/divider)*(pixArr.getHeight()/divider)][4];
		for(int y = 0; y < pixArr.getHeight()/divider; y++){
			for(int x = 0; x < pixArr.getWidth()/divider; x++){
				int[] rgba = pixArr.getRGBA(x*divider, y*divider);
				setRGBA(x, y, new int[]{rgba[0],rgba[1],rgba[2],rgba[3]});
			}
		}
	}
	
	public PixelArray(BufferedImage img){
		imageWidth = img.getWidth();
		rgbaPixels = new int[img.getWidth()*img.getHeight()][4];
		for(int y = 0; y < img.getHeight(); y++){
			for(int x = 0; x < img.getWidth(); x++){
				setRGBA(x, y, separateRGBA(img.getRGB(x, y)));
			}
		}
	}
	
	public PixelArray(BufferedImage img, int divider){
		this.divider = divider;
		imageWidth = img.getWidth()/divider;
		rgbaPixels = new int[(img.getWidth()/divider)*(img.getHeight()/divider)][4];
		for(int y = 0; y < img.getHeight()/divider; y++){
			for(int x = 0; x < img.getWidth()/divider; x++){
				setRGBA(x, y, separateRGBA(img.getRGB(x*divider, y*divider)));
			}
		}
	}
	
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
		rgbaArray[0] = (rgba >> 16) & 0xFF;
		rgbaArray[1] = (rgba >> 8) & 0xFF;
		rgbaArray[2] = rgba & 0xFF;
		rgbaArray[3] = (rgba >> 24) & 0xFF;
		return rgbaArray;
	}
	
	protected static int recombineRGBA(int[] rgba){
		int rgbCompound = rgba[3]; // alpha
		rgbCompound = (rgbCompound << 8) +rgba[0];
		rgbCompound = (rgbCompound << 8) +rgba[1];
		rgbCompound = (rgbCompound << 8) +rgba[2];
		return rgbCompound;
	}
	
	protected static int[] fixOutOfBounds(int[] rgba){
		rgba[0] = rgba[0]>0 ? (rgba[0]<255 ? rgba[0]:255):0;
		rgba[1] = rgba[1]>0 ? (rgba[1]<255 ? rgba[1]:255):0;
		rgba[2] = rgba[2]>0 ? (rgba[2]<255 ? rgba[2]:255):0;
		rgba[3] = rgba[3]>0 ? (rgba[3]<255 ? rgba[3]:255):0;
		return rgba;
	}
	
}