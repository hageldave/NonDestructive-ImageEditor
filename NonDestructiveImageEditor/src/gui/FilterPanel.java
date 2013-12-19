package gui;

import gui.util.ImageRendererListModel;
import gui.util.PhotoCornersLayout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;

import manipulators.image.ImageBlurManipulation;
import manipulators.image.ImageColorMixManipulation;
import manipulators.image.ImageContrastManipulation;
import manipulators.image.ImageManipulation;
import manipulators.image.ImageNormalMapGenerator;
import manipulators.image.ImageSaturationManipulation;
import model.DataOverview;

@SuppressWarnings("serial")
public class FilterPanel extends JPanel{
	
	JList<ImageManipulation> filterlist;

	public FilterPanel() {
		this.setLayout(new PhotoCornersLayout());
		JScrollPane scrollpane = new JScrollPane();
		filterlist = new JList<ImageManipulation>();
		filterlist.setCellRenderer(new ListCellRenderer<ImageManipulation>() {
			Color selectedColor = new Color(0x6999BB);
			
			@Override
			public Component getListCellRendererComponent(
					JList<? extends ImageManipulation> list,
					ImageManipulation value, int index, boolean isSelected,
					boolean cellHasFocus) {
					JLabel label = new JLabel(value.getManipulationName());
					if(isSelected){
						if(cellHasFocus){
							label.setBorder(BorderFactory.createLineBorder(selectedColor, 2, false));
						} else {
							label.setBorder(BorderFactory.createDashedBorder(selectedColor));
						}
					}
					if(!value.isEnabled()){
						label.setOpaque(true);
						label.setBackground(Color.darkGray);
						label.setForeground(Color.lightGray);
					}
				return label;
			}
			
		});
		final ImageRendererListModel listmodel = new ImageRendererListModel(DataOverview.renderer);
		filterlist.setModel(listmodel);
		filterlist.addMouseListener(new DragListener(filterlist, listmodel));
		filterlist.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(SwingUtilities.isRightMouseButton(e))
					showPopup(e);
			}
			
			void showPopup(MouseEvent e){
				 int index = filterlist.locationToIndex(e.getPoint());

		         if (index != -1 && filterlist.getCellBounds(index, index).contains(e.getPoint())) {
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
		
		Action effectUp = new AbstractAction("up") {
			@Override
			public void actionPerformed(ActionEvent e) {
				int elementindex = filterlist.getSelectedIndex();
				if(elementindex > -1){
					boolean success = listmodel.liftElement(elementindex, 1);
					if(success){
						filterlist.setSelectedIndex(elementindex-1);
					}
				}
			}
		};
		Action effectDown = new AbstractAction("down") {
			@Override
			public void actionPerformed(ActionEvent e) {
				int elementindex = filterlist.getSelectedIndex();
				if(elementindex > -1){
					boolean success = listmodel.liftElement(elementindex, -1);
					if(success){
						filterlist.setSelectedIndex(elementindex+1);
					}
				}
			}
		};
		Action effectDump = new AbstractAction("dump") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int elementindex = filterlist.getSelectedIndex();
				if(elementindex > -1){
					if(listmodel.removeElementAt(elementindex)){
						filterlist.setSelectedIndex(elementindex < listmodel.getSize()? elementindex:elementindex-1);
					}
				}
			}
		};
		final JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		toolbar.add(effectUp);
		toolbar.add(effectDown);
		toolbar.add(effectDump);
		
		Action[] addEffectactions = new Action[]{
				new AddEffectAction(filterlist, listmodel, ImageColorMixManipulation.class, "add Color Mixer"),
				new AddEffectAction(filterlist, listmodel, ImageContrastManipulation.class, "add Contrast"),
				new AddEffectAction(filterlist, listmodel, ImageSaturationManipulation.class, "add Saturation"),
				new AddEffectAction(filterlist, listmodel, ImageNormalMapGenerator.class, "add Normalmap Maker"),
				new AddEffectAction(filterlist, listmodel, ImageBlurManipulation.class, "add Blur")};
		
		final JPopupMenu addeffectsmenu = new JPopupMenu();
		for(Action action: addEffectactions){
			addeffectsmenu.add(action);
		}
		Action showpopupaction = new AbstractAction("Add Effect") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				addeffectsmenu.show(toolbar, 0, 0);
			}
		};
		
		toolbar.add(showpopupaction);
		this.add(toolbar, "topleft(0,-40)bottomright(1.0,1.0)");
	}
	
	
	@Override
	public Dimension getPreferredSize() {
		return getMinimumSize();
	}
	
	
	private static class AddEffectAction extends AbstractAction {
		JList<ImageManipulation> myFilterlist;
		ImageRendererListModel listmodel;
		Class<? extends ImageManipulation> clazz;
		
		
		
		public AddEffectAction(JList<ImageManipulation> list, ImageRendererListModel model, Class<? extends ImageManipulation> clazz, String actionname) {
			super(actionname);
			this.myFilterlist = list;
			this.listmodel = model;
			this.clazz = clazz;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			int selectindex = myFilterlist.getSelectedIndex();
			try {
				if(selectindex != -1 && myFilterlist.getModel().getSize() != 0){
					listmodel.addElementAt(selectindex+1, clazz.newInstance());
				} else {
					listmodel.addElement(clazz.newInstance());
				}
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
	
	private static class DragListener extends MouseAdapter {
		
		private boolean isPressed = false;
		private JList<ImageManipulation> list;
		private ImageRendererListModel model;
		private int selectionIndex = -1;
		private static final boolean debug = false;
		

		public DragListener(JList<ImageManipulation> list, ImageRendererListModel model) {
			this.list = list;
			this.model = model;
		}

		@Override
		public void mousePressed(MouseEvent e) {
			this.isPressed = true;
			debugMSG("drag start");
			int index = list.locationToIndex(e.getPoint());
			if (index != -1 && list.getCellBounds(index, index).contains(e.getPoint())){
				this.selectionIndex = index;
				debugMSG("draggin item at index " + index);
			}
				
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			this.isPressed = false;
			debugMSG("drag stop");
			int index = list.locationToIndex(e.getPoint());
			if (index != -1 && this.selectionIndex != -1 && list.contains(e.getPoint())){
				int levelDifference = selectionIndex - index;
				debugMSG("putting item to location of index " + index);
				model.liftElement(selectionIndex, levelDifference);
			}
			selectionIndex = -1;
		}

		@Override
		public void mouseDragged(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		private void debugMSG(String value){
			if(debug){
				System.out.println();
			}
		}
		
	}
	
}
