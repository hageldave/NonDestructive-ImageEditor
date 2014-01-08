package main;

import gui.MainFrame;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import manipulators.image.ImageColorMixManipulation;
import manipulators.image.ImageContrastManipulation;
import manipulators.image.ImageNormalMapGenerator;
import manipulators.image.ImageSaturationManipulation;
import model.DataOverview;

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				MainFrame frame = MainFrame.getInstance();
				frame.setMinimumSize(new Dimension(400, 300));
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				frame.setVisible(true);
			}
		});
	}
}
