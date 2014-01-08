package IO;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JFrame;

import render.PostRenderAction;
import model.DataOverview;

@SuppressWarnings("serial")
public class SaveImageAction extends AbstractAction {

	JFrame frame;
	
	public SaveImageAction(JFrame frame) {
		super("save..");
		this.frame = frame;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		FileDialog filedialog = new FileDialog(frame, "Save Image", FileDialog.SAVE);
		filedialog.setMultipleMode(false);
		filedialog.setFilenameFilter(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.matches("([^\\s]+(\\.(?i)(jpg|jpeg|png|gif|bmp))$)");
			}
		});
		filedialog.setVisible(true);
		final File[] fa = filedialog.getFiles();
		
		
			try {
				if (fa.length > 0 && fa[0] != null && (fa[0].exists() || fa[0].createNewFile())) {
					DataOverview.renderer.executeRendering(false, new PostRenderAction("save img") {
						
						@Override
						public void doPostAction(BufferedImage image, boolean wasCancelled) {
							if(image == null){ return;}
							
							System.out.println(fa[0]);
							try {
							if (fa[0].getName().matches("([^\\s]+(\\.(?i)(jpg|jpeg))$)")) {
								BufferedImage converted = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
								Graphics g = converted.getGraphics();
								g.setColor(Color.white);
								g.fillRect(0, 0, converted.getWidth(), converted.getHeight());
								g.drawImage(image, 0, 0, null);
								g.dispose();
								ImageIO.write(
										converted,
										"jpg", fa[0]);
							} else if(fa[0].getName().matches("([^\\s]+(\\.(?i)(png))$)")){
								ImageIO.write(
										image,
										"png", fa[0]);
							} else if(fa[0].getName().matches("([^\\s]+(\\.(?i)(gif))$)")){
								ImageIO.write(
										image,
										"gif", fa[0]);
							} else {
								BufferedImage converted = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
								Graphics g = converted.getGraphics();
								g.setColor(Color.white);
								g.fillRect(0, 0, converted.getWidth(), converted.getHeight());
								g.drawImage(image, 0, 0, null);
								g.dispose();
								ImageIO.write(
										converted,
										"bmp", fa[0]);
							}
							System.out.println("exported");
							frame.requestFocus();
							} catch (IOException ex) {
								ex.printStackTrace();
							}
						}
					});
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		
		
	}

}
