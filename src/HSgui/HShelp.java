package HSgui;

import javax.swing.*;

public class HShelp {
	public HShelp(){
		JFrame main = new JFrame("Help V-" + HSedit.version);
		Box big = Box.createHorizontalBox();
		JTextArea ta = new JTextArea("HotSounds Sound System"                   
					+ "\n"                                                            
					+ "\nThe Edit Window:"                                            
					+ "\n"                                                            
					+ "\nYou can use the edit window to add sounds and fades."         	
					+ "\nCommands, unless otherwise specified, effect the active sound:"
					+ "\n#fade:<number of secs, \"4.3\">"
					+ "\n     fades out in given seconds"
					+ "\n#move:<number of secs> to:<final volume, 0 to 1>"
					+ "\n     shifts the volume to the given volume in given seconds"
					+ "\n#kill"
					+ "\n     silences sound, instance can not fade up again,"
					+ "\n     however, more instances of the sound can be created"
					+ "\n#mute"
					+ "\n     silences sound, but the instance can be faded up"
					+ "\n     again in the future"
					+ "\n#scroll"
					+ "\n     Lets the mouse control wheel scroll wheel control the"
					+ "\n     volume on the active sound"
					+ "\n#reset"
					+ "\n     kills all sound and resets all instance numbers"
					+ "\n     as well as the active sound stack"
					+ "\n     (internaly, this deals with memory issues)"
					+ "\n#last"
					+ "\n     sets the active sound backwards one"
					+ "\n#live"
					+ "\n     sets the active sound to the most recent one played"
					+ "\n"
					+ "\nFor Sounds:"                                                 
					+ "\n1st box - file name with the extension"                      
					+ "\n2nd box - up fade time in seconds"                           
					+ "\n3rd box - volume between 0 and 1"                            
					+ "\n"                                                            
					+ "\nThe Show Window:"                                            
					+ "\n"                                                            
					+ "\nYou press keys and the related sound plays."                 
					+ "\nThe active sound is the last sound you played,"              
					+ "\nbut the actions #last sets active the the previous sound."   
					+ "\n#live sets it back to the most recent played."               
					+ "\nThe active sound is the one that your commands affect."         
					+ "\n"                                                            
					+ "\nThe following file formats are supported:"                   
					+ "\n.wav"                                                        
					+ "\n.mp3"                                                        
					+ "\n.aif"                                                        
					+ "\n.aiff"                                                       
					+ "\n*mp3s are supported by converting them into .wav, sorry"   );  
		ta.setEditable(false);
		ta.setWrapStyleWord(true);
		big.add(new JLabel("   "));
		big.add(ta);
		big.add(new JLabel("   "));
		main.add(big);
		main.pack();
		main.setLocation(600,100);
		main.setVisible(true);
	}

}
