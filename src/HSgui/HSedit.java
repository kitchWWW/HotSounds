/**License:
 * 
 * Obey the following when stealing/ borrowing/ selling/ commandeering/ legitimately using this code.
 * pretty please with a cherry on top.
 * 
 * In response to the new legal guidelines surrounding this glorious medium of code,
 * I hereby proclaim that I am not allowing anyone to touch my work with their filthy hands.
 * Please wash them before handling any of my intellectual property, or at the very least use hand sanitizer.
 * 
 * Compiled by the Legal Team at Tomunchi Co. without their knowledge or concent.
 * Yes, this is an unlicensed license.
 * Coppyright 2014
 */
package HSgui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;


public class HSedit {
	
	JFrame main;
	Box big;
	Box editArea;
	JScrollPane editAreaSP;
	Box menu;
	String myFilePath;
	ArrayList<String> urls;
	ArrayList<String> fadeUps;
	ArrayList<String> commands;
	ArrayList<String> keys;
	ArrayList<String> names;
	ArrayList<String> fadeUp;
	ArrayList<String> playVols;
	int x;
	int y;
	HSshowfile sf;
	String respath;
	HSedit me;
	Box namebox = Box.createVerticalBox();
	Box keybox = Box.createVerticalBox();
	Box contentbox = Box.createVerticalBox();
	Box sounds = Box.createHorizontalBox();
	Box urlbox = Box.createVerticalBox();
	Box fadebox = Box.createVerticalBox();
	Box commandbox = Box.createVerticalBox();
	Box playVolsbox = Box.createVerticalBox();
	public static final String version = "1.2.5";
	
	public HSedit(HSshowfile showfile, String filePath){
		
	//Initialize a bunch of stuff
		me = this;
		sf = showfile;
		respath = sf.getResPath();
		urls = sf.getUrls();
		fadeUps = sf.getFadeUps();
		commands = sf.getCommands();
		keys = sf.getKeys();
		names = sf.getNames();
		playVols = sf.getPlayVols();
		main = new JFrame("HS Show Editor V-" + HSedit.version);
		big = Box.createVerticalBox();
		editArea = Box.createHorizontalBox();
		editAreaSP = new JScrollPane(editArea);
		myFilePath = filePath;
		menu = Box.createHorizontalBox();
	//put together a menu bar	
		JMenuBar jmb = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu editMenu = new JMenu("Edit");
		JMenu showMenu = new JMenu("Show");
	//The file menu parts	
		JMenuItem newShow = new JMenuItem("New");
		JMenuItem loadShow = new JMenuItem("Load");
		JMenuItem saveShow = new JMenuItem("Save");
		JMenuItem saveAsShow = new JMenuItem("Save As");
		JMenuItem closeShow = new JMenuItem("Close");
		JMenuItem setShowFile = new JMenuItem("Select Source Folder");
	//The edit menu parts	
		JMenuItem addSoundMenuItem = new JMenuItem("Add Sound");
		JMenuItem addCommandMenuItem = new JMenuItem("Add Command");
		JMenuItem removeEmptyMenuItem = new JMenuItem("Remove Empties");
	//The show menu parts	
		JMenuItem goToShow = new JMenuItem("Goto Show");
		JMenuItem help = new JMenuItem("Help");
	//And give them all hot keys, sort of...	
		newShow.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.META_MASK));
		loadShow.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.META_MASK));
		saveShow.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.META_MASK));
		addSoundMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, ActionEvent.META_MASK));
		addCommandMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, ActionEvent.META_MASK));
		removeEmptyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.META_MASK));
		goToShow.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.META_MASK));
		closeShow.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.META_MASK));
	//and put it all together	
		fileMenu.add(newShow);
		fileMenu.add(loadShow);
		fileMenu.add(saveShow);
		fileMenu.add(saveAsShow);
		fileMenu.add(closeShow);
		fileMenu.add(setShowFile);
		editMenu.add(addSoundMenuItem);
		editMenu.add(addCommandMenuItem);
		editMenu.add(removeEmptyMenuItem);
		showMenu.add(goToShow);
		showMenu.add(help);
		jmb.add(fileMenu);
		jmb.add(editMenu);
		jmb.add(showMenu);
		main.setJMenuBar(jmb);
	//and make the menu bar actualy do stuff	
		newShow.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				new HSedit(new HSshowfile(),null);
			}
		});
		saveAsShow.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				int retval = chooser.showSaveDialog(null);
				updateSF();
				if(retval==JFileChooser.APPROVE_OPTION){
					File f = new File(chooser.getSelectedFile().getAbsolutePath()+".hssf");
					myFilePath = f.getAbsolutePath();
					me.save();
				}
			}
		});
		help.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				new HShelp();
			}
		});
		goToShow.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				updateSF();
				main.dispose();
				new HSshow(sf,myFilePath);
			}
		});
		setShowFile.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int retval = chooser.showOpenDialog(null);
				if(retval==JFileChooser.APPROVE_OPTION){
					File f = new File(chooser.getSelectedFile().getAbsolutePath());
					respath = f.getAbsolutePath();
					updateSF();
				}
			}
		});
		loadShow.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new FileFilter(){
					public boolean accept(File f) {
						if(f.getAbsolutePath().endsWith(".hssf")){
							return true;
						}
						return false;
					}
					public String getDescription() {
						return "The one that only accepts.hssf";
					}
					
				});
			    int returnVal = chooser.showOpenDialog(null);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	try{
			    		HSshowfile m;
			    		FileInputStream fos = new FileInputStream(chooser.getSelectedFile());
				        ObjectInputStream oos = new ObjectInputStream(fos);
				        m = (HSshowfile) oos.readObject();
				        main.dispose();
				        new HSedit(m,chooser.getSelectedFile().getAbsolutePath());
				        oos.close();
			    	}catch( Exception fd){
			    		JOptionPane.showMessageDialog(null, "Something broke, sorry... \n\n" + fd.getMessage());
			    	}
			    }
			}
		});
		saveShow.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				updateSF();
				if(myFilePath!=null){
					me.save();
				}else{
					JFileChooser chooser = new JFileChooser();
					int retval = chooser.showSaveDialog(null);
					if(retval==JFileChooser.APPROVE_OPTION){
						File f = new File(chooser.getSelectedFile().getAbsolutePath()+".hssf");
						myFilePath = f.getAbsolutePath();
						me.save();
					}
				}
			}
		});
		closeShow.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				main.dispose();
			}
		});
		addCommandMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				namebox.add(new JTextField(),commandbox.getComponentCount()+1);
				keybox.add(new JTextField(),commandbox.getComponentCount()+1);
				commandbox.add(new JTextField());
				main.pack();
			}
		});
		addSoundMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				namebox.add(new JTextField());
				urlbox.add(new JTextField());
				fadebox.add(new JTextField());
				playVolsbox.add(new JTextField(""));
				keybox.add(new JTextField());
				main.pack();
			}
		});
		removeEmptyMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				removeEmpties();
			}
		});
	//put together the edit area
	//but first initialize the stuff to the bigger box
		editArea.add(namebox);
		editArea.add(keybox);
		editArea.add(contentbox);
		
	//now we can actually do stuff :D meaning we put the data in once we load things(?)
		namebox.add(new JLabel("Name"));
		keybox.add(new JLabel(" Key "));
		contentbox.add(new JLabel("Commands : URL / fade / Vol"));
	// and we should add some more stuff
		contentbox.add(commandbox);
		contentbox.add(sounds);
		sounds.add(urlbox);
		sounds.add(fadebox);
		sounds.add(playVolsbox);
		for(String n:names){
			namebox.add(new JTextField(n));
		}
		for(String f: fadeUps){
			fadebox.add(new JTextField(f));
		}
		for(String c: commands){
			commandbox.add(new JTextField(c));
		}
		for(String u:urls){
			urlbox.add(new JTextField(u));
		}
		for(String k:keys){
			keybox.add(new JTextField(k));
		}
		for(String p:playVols){
			playVolsbox.add(new JTextField(p));
		}
	//and put it all in the screen	
		big.add(editAreaSP);
		main.add(big, BorderLayout.CENTER);
		main.pack();
		main.setLocationRelativeTo(null);
		main.setVisible(true);
	}
	protected void removeEmpties(){
		for(int i = 1; i<namebox.getComponentCount(); i++){
			JTextField nam = (JTextField) namebox.getComponent(i);
			JTextField key = (JTextField) keybox.getComponent(i);
			if(nam.getText().length()==0&& key.getText().length()==0){
				if(i<=commandbox.getComponentCount()){
					JTextField h = (JTextField) commandbox.getComponent(i-1);
					if(h.getText().length()==0){
						//remove some stuff
						namebox.remove(i);
						keybox.remove(i);
						commandbox.remove(i-1);
						i = 1;
						main.pack();
					}
				}else{
					if(urlbox.getComponentCount()>0){
						int numb = commandbox.getComponentCount();
						numb++;
						JTextField as = (JTextField) urlbox.getComponent(i-numb);
						JTextField sd = (JTextField) fadebox.getComponent(i-numb);
						JTextField df = (JTextField) playVolsbox.getComponent(i-numb);
						if(as.getText().length()==0 && sd.getText().length()==0 && df.getText().length()==0){
							//remove the other stuff
							namebox.remove(i);
							keybox.remove(i);
							urlbox.remove(i-numb);
							fadebox.remove(i-numb);
							playVolsbox.remove(i-numb);
							i=1;
							main.pack();
						}
					}
				}
			}
		}
	}
	protected void updateSF(){
		removeEmpties();
		ArrayList<String> na = new ArrayList<>();
		ArrayList<String> ur = new ArrayList<>();
		ArrayList<String> ke = new ArrayList<>();
		ArrayList<String> fa = new ArrayList<>();
		ArrayList<String> co = new ArrayList<>();
		ArrayList<String> pv = new ArrayList<>();
		for(Component name: namebox.getComponents()){
			try{
				na.add(((JTextField) name).getText());
			}catch(Exception e1){}
		}
		for(Component url: urlbox.getComponents()){
			try{
				ur.add(((JTextField) url).getText());
			}catch(Exception e1){}
		}
		for(Component key: keybox.getComponents()){
			try{
				ke.add(((JTextField) key).getText());
			}catch(Exception e1){}
		}
		for(Component fade: fadebox.getComponents()){
			try{
				fa.add(((JTextField) fade).getText());
			}catch(Exception e1){}
		}
		for(Component command: commandbox.getComponents()){
			try{
				co.add(((JTextField) command).getText());
			}catch(Exception e1){}
		}
		for(Component pvs: playVolsbox.getComponents()){
			try{
				pv.add(((JTextField) pvs).getText());
			}catch(Exception e1){}
		}
		sf.update(ur,ke,na, fa, co,pv,respath);
	}
	protected void save() {
		updateSF();
		try {
			FileOutputStream fos = new FileOutputStream(new File(myFilePath));
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(sf);
			oos.close();
			JOptionPane.showMessageDialog(null, "File saved sucessfuly");
		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "File unsucessfuly saved: \n\n" + e1.getLocalizedMessage());
		}
	}
	
}
