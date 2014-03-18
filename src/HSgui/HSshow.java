package HSgui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import HSengine.HSsound;

public class HSshow {
	
	JFrame main;
	HSsound ss;
	ArrayList<String> urls;
	ArrayList<Integer> cn;
	ArrayList<String> keys;
	ArrayList<String> names;
	ArrayList<String> commands;
	ArrayList<Double> fadeUps; 
	ArrayList<Double> playVols;
	ArrayList<HScommand> commandsWithPerams;
	HSshowfile sf;
	JLabel active;
	String myFilePath;
	
	public HSshow(HSshowfile showfile, String filePath){
	//deal with actually making sound
		myFilePath = filePath;
		String totalErrors ="";
		sf = showfile;
		urls = sf.getUrls();
		keys = sf.getKeys();
		names = sf.getNames();
		commands = sf.getCommands();
		fadeUps = new ArrayList<>();
		playVols = new ArrayList<>();
		commandsWithPerams =  new ArrayList<>();
		for(String s: sf.getFadeUps()){
			try{
				fadeUps.add(Double.parseDouble(s));
			}catch(Exception e){
				fadeUps.add(0.0);
				totalErrors = totalErrors +"\n your fade up '" + s +"' is not kosher";}
		}
		for(String s:sf.getPlayVols()){
			try{
				playVols.add(Double.parseDouble(s));
			}catch(Exception e){
				playVols.add(.7);
				totalErrors = totalErrors +"\n your volume '" + s +"' is not kosher";}
		}
		ss = new HSsound();
		ss.setMainURL(sf.getResPath());
		main = new JFrame("HS Show Player V-"+HSedit.version);
		cn = new ArrayList<>();
		for(int i = 0; i<commands.size(); i++){
			if(commands.get(i).startsWith("#live")){
				cn.add(HSkeylist.ActiveLive);
			}else if(commands.get(i).startsWith("#last")){
				cn.add(HSkeylist.ActivePop);
			}else if(commands.get(i).startsWith("#kill")){
				cn.add(HSkeylist.KillActiveSound);
			}else if(commands.get(i).startsWith("#mute")){
				cn.add(HSkeylist.SilenceActiveSound);
			}else if(commands.get(i).startsWith("#reset")){
				cn.add(HSkeylist.ResetSystem);
			}else if(commands.get(i).startsWith("#fade")){
				String m = commands.get(i);
				String doubl = m.substring(m.indexOf(":")+1);
				double d;
				try{
					d = Double.parseDouble(doubl);
				}catch(Exception e){
					d = 5;
					if(!doubl.endsWith("#fade")){
						totalErrors = totalErrors +"\n your fade up time '" + m +"' is not kosher";
					}
				}
				HScommand com;
				try {
					com = new HScommand(d);
				} catch (Exception e1) {
					e1.printStackTrace();
					totalErrors = totalErrors +"\n something with peramiters broke:\n"+ e1.getMessage();
					com = null;
				}
				commandsWithPerams.add(com);
				cn.add(HSkeylist.CommandStart - Math.abs(commandsWithPerams.indexOf(com)));
			}else if(commands.get(i).startsWith("#move")){
				String m = commands.get(i);
				String doubl = m.substring(m.indexOf(":")+1);
				doubl = doubl.substring(0, doubl.indexOf(" to:"));
				double d;
				try{
					d = Double.parseDouble(doubl);
				}catch(Exception e){
					d = 5;
					totalErrors = totalErrors +"\n your move time '" + m +"' is not kosher";
				}
				String time = m.substring(m.indexOf("to:")+3);
				double t;
				try{
					t = Double.parseDouble(time);
				}catch(Exception e){
					t = .5;
					totalErrors = totalErrors +"\n your volume '" + m +"' is not kosher";
				}
				HScommand com;
				try {
					com = new HScommand(d,t);
				} catch (Exception e1) {e1.printStackTrace(); com = null;}
				commandsWithPerams.add(com);
				cn.add(HSkeylist.CommandStart - Math.abs(commandsWithPerams.indexOf(com)));
			}else{
				totalErrors = totalErrors +"\n" + commands.get(i)+" is not a valid command";
				cn.add(HSkeylist.NullCommand);
			}
		}
		for(int i = 0; i< urls.size(); i++){
			try{
				cn.add(ss.addClip(urls.get(i)));
			}catch(Exception df){totalErrors = totalErrors+"\n" + df.getMessage();}
		}
		/**for(String s : keys){
			ArrayList<String> temp = new ArrayList<>();
			temp.addAll(keys);
			temp.remove(s);
			if(temp.contains(s)&&!totalErrors.contains("key actions for "+s)){
				totalErrors = totalErrors + "\nYou have duplicate key actions for "+s;
			}
		}**/
		if(!totalErrors.equals("")){
			JOptionPane.showMessageDialog(null, "Show loaded with the following errors:\n"+totalErrors);
		}
		
	//have fun being a gui	
		Box big = Box.createHorizontalBox();
		JScrollPane bigger = new JScrollPane(big);
		Box namebox = Box.createVerticalBox();
		Box keybox = Box.createVerticalBox();
		Box urlbox = Box.createVerticalBox();
		Box fadebox = Box.createVerticalBox();
		Box playVolsbox = Box.createVerticalBox();
	//add in the top boarder
		keybox.add(new JLabel(" "));
	//put in the top row of labels	
		namebox.add(new JLabel("Name     "));
		keybox.add(new JLabel("Key         "));
		urlbox.add(new JLabel("Command   "));
		fadebox.add(new JLabel("Fade in"));
		playVolsbox.add(new JLabel("   Vol"));
	//add the empty line	
		fadebox.add(new JLabel(" "));
		namebox.add(new JLabel(" "));
		urlbox.add(new JLabel(" "));
		keybox.add(new JLabel(" "));
		playVolsbox.add(new JLabel(" "));
	//put in the data from the arrays
		for(String n:names){
			namebox.add(new JLabel(n));
		}
		for(String c:commands){
			urlbox.add(new JLabel(c));
			fadebox.add(new JLabel(" "));
			playVolsbox.add(new JLabel(" "));
		}
		urlbox.add(new JLabel(" "));
		fadebox.add(new JLabel(" "));
		playVolsbox.add(new JLabel(" "));
		for(String u:urls){
			urlbox.add(new JLabel(u));
		}
		for(Double s: fadeUps){
			fadebox.add(new JLabel(" "+ s));
		}
		for(Double s: playVols){
			playVolsbox.add(new JLabel("   "+ s));
		}
		for(String k:keys){
			keybox.add(new JLabel(k));
		}
		namebox.add(new JLabel(" "),commands.size()+2);
		keybox.add(new JLabel(" "),commands.size()+3);
	//add in the second empty line	
		namebox.add(new JLabel(" "));
		keybox.add(new JLabel(" "));
		urlbox.add(new JLabel(" "));
		fadebox.add(new JLabel(" "));
		playVolsbox.add(new JLabel(" "));
	//add the active fow	
		namebox.add(new JLabel("Active:"));
		active = new JLabel("none");
		urlbox.add(active);
		fadebox.add(new JLabel(" "));
		playVolsbox.add(new JLabel(" "));
	
	// and the empty bottom row
		keybox.add(new JLabel(" "));
	//JButton time	
		JButton showedit = new JButton("Open Editor");
		showedit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				ss.reset();
				main.dispose();
				new HSedit(sf,myFilePath);
			}
		});
		keybox.add(new JLabel(" "));
	//and put it all together	
		big.add(new JLabel("     "));
		big.add(namebox);
		big.add(keybox);
		big.add(urlbox);
		big.add(fadebox);
		big.add(playVolsbox);
		big.add(new JLabel("     "));
		main.add(bigger, BorderLayout.CENTER);
		main.add(showedit, BorderLayout.SOUTH);
	//then add the thing to make the entire thing work:	
		showedit.addKeyListener(new HSkeylist(ss,cn,keys,this,names,fadeUps,playVols,commandsWithPerams));
		main.setLocation(150,150);
		main.pack();
		main.setVisible(true);
	}
	public void updateActive(String s){
		active.setText(s);
	}
}
