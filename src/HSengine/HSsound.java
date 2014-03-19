package HSengine;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import javax.sound.sampled.AudioSystem;

import javazoom.jl.converter.Converter;


public class HSsound {
	
	public final float MaxVolume;
	public final float MinVolume;
	ArrayList<HSclip> clips;
	ArrayList<Integer> playing;
	volatile double GrandMaster;			//volume, between 0 and 1
	String file;
	String version = "1.0.5";
	
	public HSsound(){
		GrandMaster = 1;
		clips = new ArrayList<HSclip>();
		playing = new ArrayList<>();
		MinVolume = -80f;
		MaxVolume = 6.0206f;
		file = "";
		//figure out how to actually play things
	}
	public void setMainURL(String url){
		file = url;
	}
	/**
	 * Adds a sound to the system.
	 * Add a sound at the URL specified in the peramiter, concatinated to the Main URL
	 * @param url the path of the file it is at
	 * @return the index of the clip that you added, or -1 if something done broke
	 * @throws Exception 
	 */
	public int addClip(String url) throws Exception{
		if(url.endsWith(".wav")||url.endsWith(".aif")||url.endsWith(".aiff")){
			HSclip clip = new HSclip(file, url);
			clips.add(clip);
			playing.add(0);
			//do something to check if the file is valid, but idk what.
			FileInputStream j = new FileInputStream(file+"/"+url);
			j.close();
			return clips.indexOf(clip);
		}else if(url.endsWith(".mp3")){
			String name = url.substring(0, url.length()-4);
			Converter con = new Converter();
			String u = file +"/"+ name;
			new File(u+".wav");
			con.convert(u+".mp3",u+".wav");
			HSclip clip = new HSclip(file,name+".wav");
			clips.add(clip);
			playing.add(0);
			return clips.indexOf(clip);
		}
		return -1;
	}
	/**
	 * Plays a clip
	 * @param clipNumber the clipNumber of the clip you would like to play
	 * @return the int that is the current play's index, the clip's play "instance number"
	 */
	public int playClip(int clipNumber){
		if(clipNumber<0||clipNumber>clips.size()){
			return -1;}
		playing.set(clipNumber,playing.get(clipNumber)+1);
		return clips.get(clipNumber).newPlay();
	}
	/**
	 * Plays a clip at the specified volume
	 * @param clipNumber the clipNumber of the clip you would like to play
	 * @param vol the double of the volume the clips starts at. Between -80 and 6
	 * @return the int that is the current play's index, the clip's "instance number"
	 */
	public int playClip(int clipNumber, double vol){
		checkVol(vol);
		checkClip(clipNumber);
		if(clipNumber<0||clipNumber>clips.size()){
			return -1;}
		playing.set(clipNumber,playing.get(clipNumber)+1);
		return clips.get(clipNumber).newPlayFromVolume(vol);
	}
	/**
	 * Fades in the clip from 0 to the specified 0 to 1 volume
	 * @param clipNumber
	 * @param vol
	 * @param millis
	 * @return the int that is the current play's index, the clip's "instance number"
	 */
	public int playClip(int clipNumber, double vol, int millis){
		checkClip(clipNumber);
		playing.set(clipNumber,playing.get(clipNumber)+1);
		return clips.get(clipNumber).newPlayFadeIn(millis, vol);
	}
	public double checkVolume(int clipNumber, int instanceNumber){
		checkClip(clipNumber,instanceNumber);
		double rawVol = clips.get(clipNumber).gainControl.get(instanceNumber).getValue();
		return HSvolume.unCalcVol(rawVol);
	}
	public void setVolume(int clipNumber,double volume){
		checkClip(clipNumber);
		checkVol(volume);
		HSclip c = clips.get(clipNumber);
		for(int i = 0; i<playing.get(clipNumber); i++){
			c.setVolume(i, volume);
		}
	}
	public void setVolume(int clipNumber ,int instanceNumber, double volume){
		checkClip(clipNumber,instanceNumber);
		checkVol(volume);
		HSclip c = clips.get(clipNumber);
		c.setVolume(instanceNumber, volume);
	}
	public void fade(int clipNumber, int instanceNumber, int millis, float volume){
		checkClip(clipNumber,instanceNumber);
		checkVol(volume);
		clips.get(clipNumber).shiftVolume(instanceNumber, millis, volume);
	}
	public void kill(int clipNumber, int instanceNumber){
		checkClip(clipNumber,instanceNumber);
		clips.get(clipNumber).kill(instanceNumber);
	}
	private void checkClip(int clipNumber) {
		if(clipNumber>=clips.size()||clipNumber<0){
			throw new IndexOutOfBoundsException();}
	}
	private void checkClip(int clipNumber, int instanceNumber){
		checkClip(clipNumber);
		if(instanceNumber>playing.get(clipNumber)||instanceNumber<0){
			throw new IndexOutOfBoundsException();
		}
	}
	private void checkVol(double vol) {
		if(vol<0||vol>1){
			throw new IndexOutOfBoundsException();}
	}
	
	public void dearGodKillAllTheNoiseNow(){
		for(int i = clips.size()-1; i>=0; i--){
			for(int j = playing.get(i)-1; j>=0;j--){
				clips.get(i).SILENCE(j);
			}
		}
	}
	
	public void reset(){
		dearGodKillAllTheNoiseNow();
		playing = new ArrayList<>();
		for(HSclip c: clips){
			c.reset();
			playing.add(0);
		}
		AudioSystem.getMixer(null).close();
		System.gc();
	}
}
