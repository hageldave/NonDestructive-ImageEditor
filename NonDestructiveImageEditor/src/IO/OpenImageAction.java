package IO;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JFrame;

import manipulators.image.ImageLayer;
import model.DataOverview;
import model.PixelArray;

@SuppressWarnings("serial")
public class OpenImageAction extends AbstractAction {

	JFrame frame;
	
	public OpenImageAction(JFrame frame) {
		super("open..");
		this.frame = frame;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		FileDialog filedialog = new FileDialog(frame, "Load Image", FileDialog.LOAD);
		filedialog.setMultipleMode(false);
		filedialog.setVisible(true);
		File[] fa = filedialog.getFiles();
		if(fa.length > 0 && fa[0]!= null && fa[0].exists()){
			try {
				FileInputStream in = new FileInputStream(fa[0].getAbsolutePath());
				BufferedImage image = ImageIO.read(in);
				if(image != null){
					DataOverview.renderer.addManipulation(0, new ImageLayer(new PixelArray(image)));
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		frame.requestFocus();
	}

}
