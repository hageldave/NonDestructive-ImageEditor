package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JToolBar;

public class StatusBar extends JToolBar {

	/** singleton */
	private static StatusBar instance = new StatusBar();
	/** height of statusbar */
	private int height = 20;
	
	
	/** shows state */
	private JLabel currentStatus = new JLabel("",JLabel.RIGHT);
	
	/** shows progress (of some process) */
	private JProgressBar currentProgress = new JProgressBar(0, 100);	
	
	/** color for status text */
	private final Color statusColor = Color.gray;
	
	/** color of progressbars */
	private final Color progressbarColor = Color.gray;
	
	
	private StatusBar() {
		this.setMinimumSize(new Dimension(0, height));
		this.setPreferredSize(new Dimension(400, height));
		this.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));

		this.setLayout(new BorderLayout());
		this.setFloatable(false);

		this.currentStatus.setForeground(statusColor);
		this.currentStatus.setFont(Font.decode(Font.DIALOG));
		this.currentProgress.setForeground(progressbarColor);
		this.currentProgress.setBorder(null);
		
		this.add(currentProgress, BorderLayout.CENTER);
		this.add(currentStatus, BorderLayout.WEST);
	}
	
	/** @return the statusbar singleton */
	public static StatusBar getInstance() {
		return instance;
	}
	
	/** sets current status */
	public void setCurrentStatus(String status){
		this.currentStatus.setText(status);
	}
	
	
	public String getCurrentStatus(){
		return this.currentStatus.getText();
	}
	
	/** sets current progress [0-100] */
	public void setCurrentProgress(int percent){
		if(percent >= 0 && percent <= 100){
			this.currentProgress.setValue(percent);
		} else {
			System.err.println("cannot set progress of statusbar to " + percent + "\n"
					+ "only values between 0 and 100 are permitted");
		}
	}
	
	public int getCurrentProgress(){
		return this.currentProgress.getValue();
	}
}
