package render;

import java.awt.image.BufferedImage;

public abstract class PostRenderAction {
	
	public abstract void doPostAction(BufferedImage result, boolean wasCancelled);
}
