package DKgui;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import HSengine.HSsound;


public class DKkeylist implements KeyListener {
	
	HSsound ss;
	public static final int CommandStart = -1000;
	public static final int NullCommand = -1;
	public static final int ResetSystem = -2;
	public static final int ActivePop = -3;
	public static final int ActiveLive = -4;
	public static final int KillActiveSound = -5;
	public static final int SilenceActiveSound = -6;
	public static final int ScrollingActiveSound = -7;
	public volatile boolean scrolling = false;
	ArrayList<String> keys;
	ArrayList<Integer> cn;
	ArrayList<Integer> keyCodes;
	ArrayList<Integer> activeClip;
	int activePos;
	ArrayList<Double> fadeUps;
	DKshow show;
	ArrayList<String> names;
	ArrayList<Double> playVols;
	
	
	public DKkeylist(HSsound SoundSyst, ArrayList<Integer> clipnumb, ArrayList<String> keystopress, DKshow showGUI, ArrayList<Double> fades, ArrayList<Double> volumes){
		show = showGUI;
		ss = SoundSyst;
		cn = clipnumb;
		keys = keystopress;
		fadeUps = fades;
		playVols = volumes;
		activeClip = new ArrayList<>();
		keyCodes = new ArrayList<>();
		for(int i = 0; i<keys.size(); i++){
			if(keys.get(i).length()==1){
				int ch = (int)(keys.get(i).toCharArray()[0]);
				if(ch>96&&ch<123){
					ch= ch-32;
				}
				keyCodes.add(ch);
			}else if(keys.get(i).equalsIgnoreCase("esc")){
				keyCodes.add(27);
			}else if(keys.get(i).equalsIgnoreCase("up")){
				keyCodes.add(38);
			}else if(keys.get(i).equalsIgnoreCase("down")){
				keyCodes.add(40);
			}else if(keys.get(i).equalsIgnoreCase("left")){
				keyCodes.add(37);
			}else if(keys.get(i).equalsIgnoreCase("right")){
				keyCodes.add(39);
			}
		}
	}
	
	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {
		try{
		if(keyCodes.indexOf(e.getKeyCode())!=-1){
			int doing = cn.get(keyCodes.indexOf(e.getKeyCode()));
			if(doing>=0){
				int d = (int)(fadeUps.get(doing)*1000);
				double vol = (double)(playVols.get(doing));
				if(d==0){
					if(vol==-1){
						ss.playClip(doing);
					}else{
						ss.playClip(doing,vol);
					}
				}else{
					if(vol==-1){
						ss.playClip(doing,.7,d);
					}else{
						ss.playClip(doing,vol,d);
					}
				}
				activePos = activeClip.size();
				activeClip.add(doing);
			}
		}
		}catch(Exception exc){
			JOptionPane.showMessageDialog(null, "Something broke, sorry... \n" + exc.getMessage());
			exc.printStackTrace();
		}
	}
	public void keyReleased(KeyEvent e) {
		try{
			if(keyCodes.indexOf(e.getKeyCode())!=-1){
				int doing = cn.get(keyCodes.indexOf(e.getKeyCode()));
				if(doing>=0){
					ss.fade(doing, 0, 300, 0);
					ss.reset(doing);
				}
			}
			}catch(Exception exc){
				JOptionPane.showMessageDialog(null, "Something broke, sorry... \n" + exc.getMessage());
				exc.printStackTrace();
			}
	}
}
