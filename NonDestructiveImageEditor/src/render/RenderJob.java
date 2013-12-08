package render;

import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import model.ImageRenderer;

public class RenderJob extends SwingWorker<BufferedImage, Void>{
	
	ImageRenderer renderer;
	boolean isPreview;
	PostRenderAction afterAction;
	
	
	public RenderJob(ImageRenderer renderer, boolean isPreview, PostRenderAction after) {
		this.renderer = renderer;
		this.isPreview = isPreview;
		this.afterAction = after;
	}
	

	@Override
	protected BufferedImage doInBackground() throws Exception {
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

}
