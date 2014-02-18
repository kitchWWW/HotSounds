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

import mp3Engine.MP3Sound;

public class example {
	
	public example(){
		MP3Sound ss = new MP3Sound();
		int bangSound = ss.addClipRelitive("/res/bang.mp3");
		int Flowers = ss.addClip("/users/admin/documents/zother/sounds/flowers.mp3");
		int backgroundFirstBang = ss.loopClip(bangSound);
		int flowersFirst = ss.playClip(Flowers);
		try {Thread.sleep(10000);} catch (InterruptedException e) {e.printStackTrace();}
		ss.stopClip(bangSound, backgroundFirstBang);
		ss.stopClip(Flowers, flowersFirst);
	}
}
