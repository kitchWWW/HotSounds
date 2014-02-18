import HSgui.HSedit;
import HSgui.HSshowfile;


public class HotSounds {

	public static void main(String[] args) {
		int i = 0;
		if(i==0){
			new HSedit(new HSshowfile(),"");
		}
		if(i==1){
			new HStest();
			try {
				Thread.sleep(240000);} catch (InterruptedException e) {}
		}
		if(i==2){
			new example();
		}
	}

}
