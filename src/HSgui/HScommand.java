package HSgui;

public class HScommand {
	
	public static int fade = 1;
	public static int move = 2;
	int mytype;
	int t;
	float vol;
	
	public HScommand(double time){
		t = (int) (1000*time);
		mytype = fade;
	}
	public HScommand(double time, double volume){
		mytype = move;
		t = (int) (1000*time);
		vol = (float) volume;
	}
	public String toString(){
		return mytype +" " + t + " " + vol;
	}
	public int t(){
		return t;
	}
	public float vol(){
		return vol;
	}

}
