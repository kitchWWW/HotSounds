package HSgui;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import HSengine.HSsound;

public class HSMouseWheelList implements MouseWheelListener {

	public volatile double clicks;
	HSsound ss;
	HSkeylist kl;
	double sensitivity;
	double limbeforeclick;
	
	public HSMouseWheelList(HSsound SoundSystem, HSkeylist KeyList){
		clicks = 0.0;
		ss = SoundSystem;
		kl = KeyList;
		sensitivity = 70;
		limbeforeclick = .1;
	}
	
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(kl.scrolling){
			clicks = -e.getPreciseWheelRotation();
			int clipnumb = kl.activeClip();
			int instnumb = kl.activeInstance();
			if(clipnumb>-1&&instnumb>-1){
				double curVol = ss.checkVolume(clipnumb, instnumb);
				double changeVol = clicks/sensitivity;
				if(changeVol>limbeforeclick){
					changeVol = limbeforeclick;
				}else if(Math.abs(changeVol)>limbeforeclick){
					changeVol = -limbeforeclick;
				}
				double newVol = (changeVol+curVol);
				if(newVol>1){
					newVol = 1;
				}else if(newVol<0){
					newVol = 0;
				}
				ss.setVolume(clipnumb, instnumb,newVol);
			}
		}
	}
}
