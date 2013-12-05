package manipulators.image;

import model.PixelArray;

public abstract class ImageColorManipulation extends ImageManipulation {

	@Override
	public PixelArray manipulatePixels(PixelArray image) {
		for(int i = 0; i < image.getNumOfPixels(); i++){
			image.setRGBAatIndex(i, changePixelColor(image.getRGBAatIndex(i)));
		}
		return image;
	}
	
	public abstract int[] changePixelColor(int[] rgba);

}
