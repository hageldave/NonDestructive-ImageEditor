package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel {
	BufferedImage image;
	public static ImageObserver obs = new ImageObserver() {
		
		@Override
		public boolean imageUpdate(Image img, int infoflags, int x, int y,
				int width, int height) {
			
			return (infoflags & ALLBITS) != 0; 
		}
	};
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if(image != null){
			float iWidth = image.getWidth();
			float iHeight = image.getHeight();
			float iRatio = iWidth/iHeight;
			float pWidth = this.getWidth();
			float pHeight = this.getHeight();
			float pRatio = pWidth / pHeight;
			if(pRatio > iRatio){
				// panel is too wide
				iHeight = pHeight;
				iWidth = iHeight * iRatio;
				g.drawImage(image, (int)((pWidth*0.5f)-(iWidth*0.5f)), 0, (int)iWidth, (int)iHeight, obs);
			} else {
				// panel is too high
				iWidth = pWidth;
				iHeight = iWidth / iRatio;
				g.drawImage(image, 0, (int)((pHeight*0.5f)-(iHeight*0.5f)), (int)iWidth, (int)iHeight, obs);
			}
		}
	}
	
	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(800, 600);
	}
	
}