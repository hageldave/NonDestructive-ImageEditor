package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;

import render.PostRenderAction;
import IO.OpenImageAction;
import IO.SaveImageAction;
import model.DataOverview;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	
	ImagePanel iPanel;
	FilterPanel fPanel;
	JMenuBar menubar;

	public MainFrame() {
		menubar = new JMenuBar();
		JMenu menu1 = new JMenu("File");
		JMenuItem itemopen = new JMenuItem(new OpenImageAction(this));
		JMenuItem itemsave = new JMenuItem(new SaveImageAction(this));
		menu1.add(itemopen);
		menu1.add(itemsave);
		menubar.add(menu1);
		this.setJMenuBar(menubar);
		
		Container contentpane = this.getContentPane();
		contentpane.setLayout(new BorderLayout());
		iPanel = new ImagePanel();
		fPanel = new FilterPanel();
		JSplitPane splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, iPanel, fPanel);
		splitpane.setDividerLocation(800);
		contentpane.add(splitpane);
		// TODO: better listener creation and location of creation
		final PostRenderAction ipanelUpdate = new PostRenderAction("ipanel") {
			@Override
			public void doPostAction(BufferedImage result, boolean wasCancelled) {
				if(!wasCancelled){
					iPanel.setImage(result);
					iPanel.repaint();
				}
			}
		};
		DataOverview.renderer.addObserver(new Observer() {
			
			@Override
			public void update(Observable arg0, Object arg1) {
				DataOverview.renderer.executeRendering(true, ipanelUpdate);
			}
		});
	}
	
}
