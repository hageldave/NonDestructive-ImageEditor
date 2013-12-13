package model;

import java.awt.image.BufferedImage;

public interface IPixelCollection {

	public abstract int[] getRGBA(int x, int y);

	public abstract int[] getRGBAatIndex(int index);

	public abstract void setRGBA(int x, int y, int[] rgba);

	public abstract void setRGBAatIndex(int index, int[] rgba);

	public abstract int getHeight();

	public abstract int getWidth();

	public abstract int getNumOfPixels();

	public abstract BufferedImage generateBufferedImage();

}