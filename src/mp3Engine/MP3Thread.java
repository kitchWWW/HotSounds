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
package mp3Engine;

import java.io.FileInputStream;
import javazoom.jl.player.Player;

public class MP3Thread implements Runnable {
	
	String filePath;
	volatile Player pl;
	volatile boolean loop;
	Boolean relitive;
	/**
	 * Actualy does the playing. Initlizing the thread starts the clip playing, so you don't need to tell it to .start()
	 * @param relitiveURL is whether or not the url is relitive
	 * @param FilePath the path of the file
	 * @param DoYouWantToLoop is whether or not you want to loop insecently.
	 */
	public MP3Thread(Boolean relitiveURL, String FilePath, boolean DoYouWantToLoop){
		filePath = FilePath;
		relitive = relitiveURL;
		loop = DoYouWantToLoop;
		Thread t = new Thread(this);
		try {
			if(!relitive){
				pl = new Player(new FileInputStream(filePath));
			}else{
				pl = new Player(this.getClass().getResourceAsStream(filePath));
			}
		} catch (Exception e) {e.printStackTrace();}
		t.start();
	}
	/**
	 * stops the clip from playing. Also stops looping.
	 */
	public void stop() {
		pl.close();
		loop = false;
	}
	public void run() {
		try {
			pl.play();
			while(loop){
				if(!relitive){
					pl = new Player(new FileInputStream(filePath));
				}else{
					pl = new Player(this.getClass().getResourceAsStream(filePath));
				}
				pl.play();
			}
		}catch(Exception e){e.printStackTrace();}
	}
}
