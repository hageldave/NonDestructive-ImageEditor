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
		float xf = (x*1f)/width;
		float yf = (y*1f)/height;
		float xt = baseImage.getWidth()*xf;
		float yt = baseImage.getHeight()*yf;
		
		int x1T = (int)xt;
		int x2T = (int)(xt+1f); x2T = x2T < baseImage.getWidth() ? x2T:x1T;
		int y1T = (int)yt;
		int y2T = (int)(yt+1f); y2T = y2T < baseImage.getHeight()? y2T:y1T;
		
		int[] rgba = new int[4];
		
		int[] rgba1 = baseImage.getRGBA(x1T, y1T);
		int[] rgba2 = baseImage.getRGBA(x1T, y2T);
		int[] rgba3 = baseImage.getRGBA(x2T, y1T);
		int[] rgba4 = baseImage.getRGBA(x2T, y2T);
		
		
//		rgba[0] = (int)(rgba1[0]*(1f-((xt-x1T)+(yt-y1T))/2f)+rgba2[0]*(1f-((xt-x1T)+(y2T-yt))/2f)+rgba3[0]*(1f-((x2T-xt)+(yt-y2T))/2f)+rgba4[0]*(1f-((x2T-xt)+(y2T-yt))/2f));
//		rgba[1] = (int)(rgba1[1]*(1f-((xt-x1T)+(yt-y1T))/2f)+rgba2[1]*(1f-((xt-x1T)+(y2T-yt))/2f)+rgba3[1]*(1f-((x2T-xt)+(yt-y2T))/2f)+rgba4[1]*(1f-((x2T-xt)+(y2T-yt))/2f));
//		rgba[2] = (int)(rgba1[2]*(1f-((xt-x1T)+(yt-y1T))/2f)+rgba2[2]*(1f-((xt-x1T)+(y2T-yt))/2f)+rgba3[2]*(1f-((x2T-xt)+(yt-y2T))/2f)+rgba4[2]*(1f-((x2T-xt)+(y2T-yt))/2f));
//		rgba[3] = (int)(rgba1[3]*(1f-((xt-x1T)+(yt-y1T))/2f)+rgba2[3]*(1f-((xt-x1T)+(y2T-yt))/2f)+rgba3[3]*(1f-((x2T-xt)+(yt-y2T))/2f)+rgba4[3]*(1f-((x2T-xt)+(y2T-yt))/2f));
		rgba[0] = rgba1[0];
		rgba[1] = rgba1[1];
		rgba[2] = rgba1[2];
		rgba[3] = rgba1[3];
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
