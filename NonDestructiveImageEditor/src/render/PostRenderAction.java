package render;

import java.awt.image.BufferedImage;

public abstract class PostRenderAction {
	
	private String id;
	
	
	public PostRenderAction(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public abstract void doPostAction(BufferedImage result, boolean wasCancelled);
}
