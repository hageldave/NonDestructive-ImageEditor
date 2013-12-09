package render;

import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

public class RenderJob extends SwingWorker<BufferedImage, Void>{
	
	ImageRenderer renderer;
	boolean isPreview;
	PostRenderAction afterAction;
	
	
	public RenderJob(ImageRenderer renderer, boolean isPreview, PostRenderAction after) {
		this.renderer = renderer;
		this.isPreview = isPreview;
		this.afterAction = after;
	}
	

	public boolean isPreview() {
		return isPreview;
	}


	@Override
	protected BufferedImage doInBackground() throws Exception {
		Thread.yield();
		return isPreview? renderer.renderPreview():renderer.renderFull();
	}
	
	@Override
	protected void done() {
		if (this.isCancelled()) {
			afterAction.doPostAction(null, true);
		} else {
			try {
				BufferedImage result = get();
				afterAction.doPostAction(result, false);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
				afterAction.doPostAction(null, false);
			}
		}
	}
	
	
	public String getPostRenderActionID(){
		return this.afterAction.getId();
	}

}
