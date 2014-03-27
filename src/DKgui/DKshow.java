package DKgui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import HSengine.HSsound;

public class DKshow {
	
	JFrame main;
	HSsound ss;
	ArrayList<String> urls;
	ArrayList<Integer> cn;
	ArrayList<String> keys;
	ArrayList<String> names;
	ArrayList<String> commands;
	ArrayList<Double> fadeUps; 
	ArrayList<Double> playVols;
	DKshowfile sf;
	String myFilePath;
	
	public DKshow(DKshowfile showfile, String filePath){
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
		main = new JFrame("HS DrumKit V-"+DKedit.version);
		cn = new ArrayList<>();
		for(int i = 0; i< urls.size(); i++){
			try{
				cn.add(ss.addClip(urls.get(i)));
			}catch(Exception df){totalErrors = totalErrors+"\n" + df.getMessage();}
		}
		if(!totalErrors.equals("")){
			JOptionPane.showMessageDialog(null, "Show loaded with the following errors:\n"+totalErrors);
		}
		
	//have fun being a gui	
		Box big = Box.createHorizontalBox();
		JScrollPane bigger = new JScrollPane(big);
		Box keybox = Box.createVerticalBox();
		Box urlbox = Box.createVerticalBox();
		Box fadebox = Box.createVerticalBox();
		Box playVolsbox = Box.createVerticalBox();
	//add in the top boarder
		keybox.add(new JLabel(" "));
	//put in the top row of labels	
		keybox.add(new JLabel("Key         "));
		urlbox.add(new JLabel("Command   "));
		fadebox.add(new JLabel("Fade in"));
		playVolsbox.add(new JLabel("   Vol"));
	//add the empty line	
		fadebox.add(new JLabel(" "));
		urlbox.add(new JLabel(" "));
		keybox.add(new JLabel(" "));
		playVolsbox.add(new JLabel(" "));
	//put in the data from the arrays
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
	// and the empty bottom row
		keybox.add(new JLabel(" "));
	//JButton time	
		JButton showedit = new JButton("Open Editor");
		showedit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				ss.reset();
				main.dispose();
				new DKedit(sf,myFilePath);
			}
		});
	//and put it all together	
		big.add(new JLabel("     "));
		big.add(keybox);
		big.add(urlbox);
		big.add(fadebox);
		big.add(playVolsbox);
		big.add(new JLabel("     "));
		main.add(bigger, BorderLayout.CENTER);
		main.add(showedit, BorderLayout.SOUTH);
	//then add the thing to make the entire thing work:	
		DKkeylist kl = new DKkeylist(ss,cn,keys,this,fadeUps,playVols);
		showedit.addKeyListener(kl);
		main.setLocation(150,150);
		main.pack();
		main.setVisible(true);
	}
}
