package HSgui;

import javax.swing.*;

public class HShelp {
	public HShelp(){
		JFrame main = new JFrame("Help V-" + HSedit.version);
		Box big = Box.createHorizontalBox();
		
		JTextArea ta = new JTextArea("    HotSounds Sound System"                                +"    "
			+"\n    "		+ ""                                                                 +"    "
			+"\n    "		+ "The Edit Window:"                                                 +"    "
			+"\n    "		+ ""                                                                 +"    "
			+"\n    "		+ "You can use the edit window to add sounds and fades."              +"    "
			+"\n    "		+ "Fades with a specific downfade time are written '#fade:4'"         +"    "
			+"\n    "		+ "for a downfade time of 4. The default time is 10sec."             +"    "
			+"\n    "		+ "For Sounds:"                                                      +"    "
			+"\n    "		+ "1st box - file name with the extension"                           +"    "
			+"\n    "		+ "2nd box - up fade time in seconds"                                +"    "
			+"\n    "		+ "3rd box - volume between 0 and 1"                                 +"    "
			+"\n    "		+ ""                                                                 +"    "
			+"\n    "		+ "The Show Window:"                                                 +"    "
			+"\n    "		+ ""                                                                 +"    "
			+"\n    "		+ "You press keys and the related sound plays."                      +"    "
			+"\n    "		+ "The active sound is the last sound you played,"                   +"    "
			+"\n    "		+ "but the actions #last sets active the the previous sound."        +"    "
			+"\n    "		+ "#live sets it back to the most recent played."                    +"    "
			+"\n    "		+ "The active sound is the one that your fades affect."              +"    "
			+"\n    "		+ ""                                                                 +"    "
			+"\n    "		+ "The following file formats are supported:"                        +"    "
			+"\n    "		+ ".wav"                                                             +"    "
			+"\n    "		+ ".mp3"                                                             +"    "
			+"\n    "		+ ".aif"                                                             +"    "
			+"\n    "		+ ".aiff"                                                            +"    "
			+"\n    "		+ "*mp3s are supported by converting them into .wav, sorry"          +"    "
			+"\n"
			);
		ta.setEditable(false);
		ta.setWrapStyleWord(true);
		big.add(ta);
		main.add(big);
		main.pack();
		main.setVisible(true);
	}

}
