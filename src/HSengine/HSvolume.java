package HSengine;

import javax.sound.sampled.FloatControl;

public class HSvolume implements Runnable {

	int mil;
	float stopVol;
	float startVol;
	FloatControl gainControl;
	long timeStart;
	long timeNow;
	long timeChange;
	float changeVol;
	volatile boolean unBrokenFade;
	
	//they should pass this the fake volumes, from 0 to 1
	
	/**HSvolume object
	 * This is a cool do-dad to fade stuff, it works pretty well, especially with longer fade times.
	 * If you want to cut off this fade, you need to call the breakFade() method to stop the loop
	 * @param gainControl the thing to fade it on
	 * @param millis	The time in milliseconds the fade should take
	 * @param targetVolume	The volume from 0 to 1.
	 */
	public HSvolume(FloatControl gainControl2, int millis, float targetVolume){
		mil = millis;
		stopVol = targetVolume;
		gainControl = gainControl2;
		startVol = HSvolume.unCalcVol(gainControl.getValue());
		changeVol = targetVolume-startVol;
		//start vol and change vol are between 0 and 1
		Thread t = new Thread(this);
		t.start();
	}
	/**
	 * You give it the volume between 0 and 1
	 * @param vol
	 * @return the float between -80 and 6.02
	 */
	public static float calcVol(double vol){
		if(vol>1){
			return 6.02f;
		}
		if(vol<-80){
			return -80.0f;
		}
		float ret = (float) (Math.log10(vol)/Math.log10(1.1)+6);
		if(ret<=-80){
			return -80;
		}
		return ret;
	}
	/**
	 * you give it the volume between -80 and 6.02
	 * @param vol
	 * @return the volume between 0 and 1
	 */
	public static float unCalcVol(double vol){
		if(vol>6.02){
			return 1;
		}
		if(vol<-80){
			return 0;
		}
		float ret = (float) (Math.pow(1.1, vol-6));
		if(ret>6){
			return 6;
		}
		return ret;
	}
	public void breakFade(){
		unBrokenFade = false;
	}
	
	public void run() {
		timeStart = System.currentTimeMillis();
		unBrokenFade = true;
		if(startVol<stopVol){
			loop:
			while(HSvolume.unCalcVol(gainControl.getValue())<stopVol&&unBrokenFade){
				timeNow = System.currentTimeMillis();
				timeChange = timeNow-timeStart;
				// tobeVolume is right now is the 0 to 1 version of the final volume
				float tobeVolume = (((timeChange/(float)mil)*changeVol) + startVol);
				if(HSvolume.calcVol(tobeVolume)>=gainControl.getMaximum()){
					gainControl.setValue(gainControl.getMaximum());
					break loop;
				}
				gainControl.setValue(HSvolume.calcVol(tobeVolume));
			}
		}else{
			loop:
				while(HSvolume.unCalcVol(gainControl.getValue())>stopVol&&unBrokenFade){
					timeNow = System.currentTimeMillis();
					timeChange = timeNow-timeStart;
					// tobeVolume is right now is the 0 to 1 version of the final volume
					float tobeVolume = (((timeChange/(float)mil)*changeVol) + startVol);
					if(HSvolume.calcVol(tobeVolume)<=gainControl.getMinimum()){
						gainControl.setValue(gainControl.getMinimum());
						break loop;
					}
					gainControl.setValue(HSvolume.calcVol(tobeVolume));
				}
		}
	}
}
