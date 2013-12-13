package model;

import java.awt.image.BufferedImage;

public class ResolutionWrapper implements IPixelCollection {
	
	IPixelCollection baseImage;
	int width;
	int height;
	
	
	public ResolutionWrapper(IPixelCollection baseImage) {
		this.baseImage = baseImage;
		width = baseImage.getWidth();
		height = baseImage.getHeight();
	}
	
	public ResolutionWrapper(IPixelCollection baseImage, int width, int height) {
		this.baseImage = baseImage;
		this.width = width;
		this.height = height;
	}
	
	

	@Override
	public int[] getRGBA(int x, int y) {
		float xf = (x*1f)/width; // x relative to width
		float yf = (y*1f)/height;
		float xt = baseImage.getWidth()*xf; // target x value (is decimal)
		float yt = baseImage.getHeight()*yf;
		
		int x1T = (int)xt; // target x rounded down
		int x2T = (int)(xt+1f); // target x rounded up
		int y1T = (int)yt;
		int y2T = (int)(yt+1f);
		
		int[] rgba = new int[4];
		
		// 4 pixels surrounding the target position (have to check if is right or bottom border)
		int[] rgba1 = baseImage.getRGBA(x1T, y1T);
		int[] rgba2 = baseImage.getRGBA(x1T, y2T < baseImage.getHeight()? y2T:y1T);
		int[] rgba3 = baseImage.getRGBA(x2T < baseImage.getWidth() ? x2T:x1T, y1T);
		int[] rgba4 = baseImage.getRGBA(x2T < baseImage.getWidth() ? x2T:x1T, y2T < baseImage.getHeight()? y2T:y1T);
		
		float factor1 = 1f-(xt-x1T); // horizontal factor for left pixels (when target x is nearer to left pixels, left values are taken more into account
		float factor2 = 1f-(x2T-xt); // horizontal factor for right pixels
		
		int avg1 = (int)((rgba1[0]*(factor1)+rgba3[0]*(factor2))*(1-(yt-y1T))); // average red of the 2 horizontal top pixels multiplied by vertical factor for top pixels
		int avg2 = (int)((rgba2[0]*(factor1)+rgba4[0]*(factor2))*(1-(y2T-yt))); // average red of the 2 horizontal bottom pixels multiplied by vertical factor for bottom pixels
		rgba[0] = (avg1+avg2); // average of all 4 surrounding pixels with distance to target point taken into account
		avg1 = (int)((rgba1[1]*(factor1)+rgba3[1]*(factor2))*(1-(yt-y1T))); // avergae green ..
		avg2 = (int)((rgba2[1]*(factor1)+rgba4[1]*(factor2))*(1-(y2T-yt)));
		rgba[1] = (avg1+avg2);
		avg1 = (int)((rgba1[2]*(factor1)+rgba3[2]*(factor2))*(1-(yt-y1T))); // average blue..
		avg2 = (int)((rgba2[2]*(factor1)+rgba4[2]*(factor2))*(1-(y2T-yt)));
		rgba[2] = (avg1+avg2);
		avg1 = (int)((rgba1[3]*(factor1)+rgba3[3]*(factor2))*(1-(yt-y1T))); // average alpha
		avg2 = (int)((rgba2[3]*(factor1)+rgba4[3]*(factor2))*(1-(y2T-yt)));
		rgba[3] = (avg1+avg2);
		
		return rgba;
	}

	@Override
	public int[] getRGBAatIndex(int index) {
		return getRGBA(index % width, index/width);
	}

	@Override
	public void setRGBA(int x, int y, int[] rgba) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRGBAatIndex(int index, int[] rgba) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getWidth() {
		return width;
	}
	
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public int getNumOfPixels() {
		return width * height;
	}

	@Override
	public BufferedImage generateBufferedImage() {
		// TODO Auto-generated method stub
		return null;
	}

}
