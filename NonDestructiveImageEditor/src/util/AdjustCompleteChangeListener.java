package util;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public abstract class AdjustCompleteChangeListener implements ChangeListener {

	@Override
	public void stateChanged(ChangeEvent ce) {
		Object src = ce.getSource();
		if(src instanceof JSlider){
			if(!((JSlider)src).getValueIsAdjusting()){
				changed(ce);
			}
		} else {
			changed(ce);
		}
	}
	
	public abstract void changed(ChangeEvent ev);

}
