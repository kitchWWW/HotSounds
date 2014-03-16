package HSgui;

import java.io.Serializable;
import java.util.ArrayList;


public class HSshowfile implements Serializable{

	private static final long serialVersionUID = 1L;
	ArrayList<String> urls;
	ArrayList<String> keys;
	ArrayList<String> names;
	ArrayList<String> fadeUps;
	ArrayList<String> commands;
	ArrayList<String> playVols;
	String resPath;
	HSshowfile me;
	
	public HSshowfile(){
		urls = new ArrayList<String>();
		keys = new ArrayList<String>();
		names = new ArrayList<String>();
		fadeUps = new ArrayList<String>();
		commands = new ArrayList<String>();
		playVols = new ArrayList<String>();
		me = this;
		resPath = "";
		addtheStuff();
	}
	
	public void update(ArrayList<String> url,ArrayList<String> key,ArrayList<String> name, ArrayList<String> fades, ArrayList<String> command, ArrayList<String> Volumes, String fileURL){
		urls = url;
		keys = key;
		names = name;
		fadeUps = fades;
		commands = command;
		playVols = Volumes;
		me = this;
		resPath = fileURL;
		addtheStuff();
	}
	private void addtheStuff(){
		if(!commands.contains("#reset")){
			commands.add("#reset");
			names.add(commands.size()-1,"reset");
			keys.add(commands.size()-1,"esc");
		}
		if(!commands.contains("#live")){
			commands.add("#live");
			names.add(commands.size()-1,"Live");
			keys.add(commands.size()-1,"right");
		}
		if(!commands.contains("#last")){
			commands.add("#last");
			names.add(commands.size()-1,"Last");
			keys.add(commands.size()-1,"left");
		}
	}
	public ArrayList<String> getUrls() {
		return urls;
	}
	public ArrayList<String> getKeys() {
		return keys;
	}
	public ArrayList<String> getNames() {
		return names;
	}
	public ArrayList<String> getFadeUps() {
		return fadeUps;
	}
	public ArrayList<String> getCommands() {
		return commands;
	}
	public ArrayList<String> getPlayVols() {
		return playVols;
	}
	public void setResPath(String url){
		resPath = url;
	}
	public String getResPath(){
		return resPath;
	}
	public boolean isEmpty(){
		if(urls.size()==0){
			return true;
		}
		return false;
	}
}
