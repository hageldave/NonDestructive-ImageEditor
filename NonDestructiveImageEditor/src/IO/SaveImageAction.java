package IO;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JFrame;

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
		File[] fa = filedialog.getFiles();
		
		try {
			if (fa.length > 0 && fa[0] != null && (fa[0].exists() || fa[0].createNewFile())) {
				BufferedImage image = DataOverview.renderer.renderFull();
				if(image == null){ return;}
				
				System.out.println(fa[0]);
				if (fa[0].getName().matches("([^\\s]+(\\.(?i)(jpg|jpeg))$)")) {
					ImageIO.write(
							image,
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
					System.out.println("this one");
					ImageIO.write(
							image,
							"bmp", fa[0]);
				}
				System.out.println("exported");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		frame.requestFocus();
	}

}
