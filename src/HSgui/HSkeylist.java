package HSgui;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import HSengine.HSsound;


public class HSkeylist implements KeyListener {
	
	HSsound ss;
	public static final int CommandStart = -1000;
	public static final int NullCommand = -1;
	public static final int ResetSystem = -2;
	public static final int ActivePop = -3;
	public static final int ActiveLive = -4;
	public static final int KillActiveSound = -5;
	public static final int SilenceActiveSound = -6;
	//scrolling still needs to be implemented, but oh well.
	public volatile boolean scrolling = false;
	ArrayList<String> keys;
	ArrayList<Integer> cn;
	ArrayList<Integer> keyCodes;
	ArrayList<Integer> activeClip;
	ArrayList<Integer> activeInstance;
	int activePos;
	ArrayList<Double> fadeUps;
	HSshow show;
	ArrayList<String> names;
	ArrayList<Double> playVols;
	ArrayList<HScommand> commands;
	
	
	public HSkeylist(HSsound SoundSyst, ArrayList<Integer> clipnumb, ArrayList<String> keystopress, HSshow showGUI, ArrayList<String> nam, ArrayList<Double> fades, ArrayList<Double> volumes, ArrayList<HScommand> coms){
		names = new ArrayList<>();
		for(int i = 0; i<clipnumb.size(); i++){
			if(clipnumb.get(i)>=0){
				names.add(nam.get(i));
			}
		}
		show = showGUI;
		ss = SoundSyst;
		cn = clipnumb;
		keys = keystopress;
		fadeUps = fades;
		playVols = volumes;
		activeClip = new ArrayList<>();
		activeInstance = new ArrayList<>();
		keyCodes = new ArrayList<>();
		commands = coms;
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
						activeInstance.add(ss.playClip(doing));
					}else{
						activeInstance.add(ss.playClip(doing,vol));
					}
				}else{
					if(vol==-1){
						activeInstance.add(ss.playClip(doing,.7,d));
					}else{
						activeInstance.add(ss.playClip(doing,vol,d));
					}
				}
				activePos = activeClip.size();
				activeClip.add(doing);
				show.updateActive(names.get(activeClip.get(activePos)) +"  "+activeInstance.get(activePos));
			}else if(activeClip.size()!=0){
				if(doing <= CommandStart){
					HScommand command = commands.get(Math.abs(doing - CommandStart));
					if(command.mytype == HScommand.fade){
						System.out.println(command);
						System.out.println("fade");
						ss.fade(activeClip.get(activePos), activeInstance.get(activePos), command.t(), 0);
					}
					else if(command.mytype == HScommand.move){
						System.out.println("move");
						ss.fade(activeClip.get(activePos), activeInstance.get(activePos), command.t(), command.vol());
					}
				}else if(doing == ResetSystem){
					show.updateActive("Killing...");
					ss.dearGodKillAllTheNoiseNow();
					ss.reset();
					activePos = 0;
					activeClip = new ArrayList<>();
					activeInstance = new ArrayList<>();
					show.updateActive("none");
				}else if(doing == ActivePop){
					try{
						activePos--;
						if(activePos<0){activePos = 0 ;}
						show.updateActive(names.get(activeClip.get(activePos)) +"  "+activeInstance.get(activePos));
					}catch(Exception emjkl){}
				}else if(doing == ActiveLive){
					activePos = activeClip.size()-1;
					show.updateActive(names.get(activeClip.get(activePos)) +"  "+activeInstance.get(activePos));
				}else if(doing == KillActiveSound){
					ss.kill(activeClip.get(activePos), activeInstance.get(activePos));
					activeClip.remove(activePos);
					activeInstance.remove(activePos);
					activePos--;
					if(activePos!=-1){
						show.updateActive(names.get(activeClip.get(activePos)) +"  "+activeInstance.get(activePos));
					}else{
						show.updateActive("none");
						activePos++;
					}
				}
			}
		}
		}catch(Exception exc){
			JOptionPane.showMessageDialog(null, "Something broke, sorry... \n" + exc.getMessage());
			exc.printStackTrace();
		}
	}
	public void keyReleased(KeyEvent e) {}
}
