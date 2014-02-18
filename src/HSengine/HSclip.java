package HSengine;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import javax.sound.sampled.*;


public class HSclip {
	
	File file;			//the file it will play
	URL url;			//the location of the file, maybe
	ArrayList<Clip> clips;
	ArrayList<Boolean> paused;
	float targetDB;
	float fadePerStep;
	ArrayList<FloatControl> gainControl;
	String fileUrl;
	double GrandMaster;
	ArrayList<HSvolume> volumeControl;
	
	public HSclip(String fileURL, String url){
		fileUrl = fileURL;
		file = new File(fileUrl+"/"+url);
		clips = new ArrayList<>();
		targetDB = 0F;
		fadePerStep = .1F;
		gainControl = new ArrayList<>();
		volumeControl = new ArrayList<>();
	}
	public HSclip(String fileURL, File f){
		file = f;
		clips = new ArrayList<>();
		targetDB = 0F;
		fadePerStep = .1F; 
		gainControl = new ArrayList<>();
		volumeControl = new ArrayList<>();
	}
	public int newPlay() {
		int ouri = clips.size();
		AudioInputStream stream = null;
		try {
			stream = AudioSystem.getAudioInputStream(file);
		}catch (Exception e) {e.printStackTrace();}
		
		AudioFormat format = stream.getFormat();
		clips.add(null);
		
		// specify what kind of line we want to create
		DataLine.Info info = new DataLine.Info(Clip.class, format);
		
		// create the line
		try {
			clips.set(ouri, (Clip)AudioSystem.getLine(info));
		}catch (Exception e) {e.printStackTrace();}
		
		// load the samples from the stream
		try {
			clips.get(ouri).open(stream);
		}catch (Exception e) {e.printStackTrace();}
		
		// begin play back of the sound clip
		clips.get(ouri).loop(0);
		gainControl.add((FloatControl) clips.get(ouri).getControl(FloatControl.Type.MASTER_GAIN));
		volumeControl.add(null);
		return ouri;
	}
	public int newPlayFromVolume(double vol){
		int ouri = clips.size();
		AudioInputStream stream = null;
		try {
			stream = AudioSystem.getAudioInputStream(file);
		}catch (Exception e) {e.printStackTrace();}
		AudioFormat format = stream.getFormat();
		clips.add(null);
		DataLine.Info info = new DataLine.Info(Clip.class, format);
		try {
			clips.set(ouri, (Clip)AudioSystem.getLine(info));
		}catch (Exception e) {e.printStackTrace();}
		try {
			clips.get(ouri).open(stream);
		}catch (Exception e) {e.printStackTrace();}
		float volume = HSvolume.calcVol(vol);
		gainControl.add((FloatControl) clips.get(ouri).getControl(FloatControl.Type.MASTER_GAIN));
		volumeControl.add(null);
		gainControl.get(gainControl.size()-1).setValue(volume);
		clips.get(ouri).loop(0);
		return ouri;
	}
	
	public void pause(int ouri){
		clips.get(ouri).stop();
	}
	public void resume(int ouri){
		clips.get(ouri).start();
	}
	
	public void reset(){
		for(HSvolume vs: volumeControl){
				if(vs!=null){
					vs.breakFade();}
				}
		for(FloatControl c: gainControl){
			c.setValue(c.getMinimum());
		}
		for(Clip c: clips){
			c.stop();
			c.flush();
			c.drain();
		}
		gainControl = new ArrayList<>();
		volumeControl = new ArrayList<>();
		clips = new ArrayList<>();
	}
	public void shiftVolume(int instanceNumber, int millis, double finalVol){
		try{volumeControl.get(instanceNumber).breakFade();
		}catch(Exception e){e.printStackTrace();}
		HSvolume v = new HSvolume(gainControl.get(instanceNumber), millis, (float) finalVol);
		volumeControl.set(instanceNumber, v);
	}
	
	public void setVolume(int instanceNumber, double volume) {
		if(volumeControl.get(instanceNumber)!=null){
			volumeControl.get(instanceNumber).breakFade();
		}
		float vol = HSvolume.calcVol(volume);
		FloatControl gc = gainControl.get(instanceNumber);
		gc.setValue((float)vol);
	}

	public void SILENCE(int instanceNumber) {
		if(volumeControl.get(instanceNumber)!=null){
			volumeControl.get(instanceNumber).breakFade();
		}
		clips.get(instanceNumber).close();
		FloatControl gainControl = (FloatControl) clips.get(instanceNumber).getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(gainControl.getMinimum());
	}

	public int newPlayFadeIn(int millis, double vol) {
		int ouri = clips.size();
		AudioInputStream stream = null;
		try {stream = AudioSystem.getAudioInputStream(file);}catch (Exception e) {e.printStackTrace();}
		AudioFormat format = stream.getFormat();
		clips.add(null);
		DataLine.Info info = new DataLine.Info(Clip.class, format);
		try {clips.set(ouri, (Clip)AudioSystem.getLine(info));}catch (Exception e) {e.printStackTrace();}
		try {clips.get(ouri).open(stream);}catch (Exception e) {e.printStackTrace();}
		gainControl.add((FloatControl) clips.get(ouri).getControl(FloatControl.Type.MASTER_GAIN));
		gainControl.get(gainControl.size()-1).setValue(HSvolume.calcVol(0));
		clips.get(ouri).loop(0);
		HSvolume asdf = (new HSvolume(gainControl.get(gainControl.size()-1), millis, (float)vol));
		volumeControl.add(asdf);
		return ouri;
	}
}
