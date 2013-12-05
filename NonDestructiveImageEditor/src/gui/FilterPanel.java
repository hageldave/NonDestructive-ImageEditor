package gui;

import gui.util.ImageRendererListModel;
import gui.util.PhotoCornersLayout;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import manipulators.image.ImageManipulation;
import model.DataOverview;

@SuppressWarnings("serial")
public class FilterPanel extends JPanel{
	
	JList<ImageManipulation> filterlist;

	public FilterPanel() {
		this.setLayout(new PhotoCornersLayout());
		JScrollPane scrollpane = new JScrollPane();
		filterlist = new JList<ImageManipulation>();
		filterlist.setCellRenderer(new ListCellRenderer<ImageManipulation>() {
			@Override
			public Component getListCellRendererComponent(
					JList<? extends ImageManipulation> list,
					ImageManipulation value, int index, boolean isSelected,
					boolean cellHasFocus) {
				return new JLabel(value.getGUIEditor().getName());
			}
			
		});
		filterlist.setModel(new ImageRendererListModel(DataOverview.renderer));
		filterlist.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				passMouseEvent(e);
			}
			
			void passMouseEvent(MouseEvent e){
				 int index = filterlist.locationToIndex(e.getPoint());

		         if (index != -1) {
		         JComponent comp = filterlist.getModel().getElementAt(index).getGUIEditor();
		         JPopupMenu popup = new JPopupMenu();
		         popup.setLayout(new BorderLayout());
		         popup.add(comp, BorderLayout.CENTER);
		         popup.show(filterlist, e.getX(), e.getY());
		         }
			}
		});
		scrollpane.setViewportView(filterlist);
		this.add(scrollpane, "topleft(0,0)bottomright(1.0,-40)");
	}
	
}
