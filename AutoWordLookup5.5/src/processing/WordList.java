package processing;

import java.util.Vector;

import dictionaries.Dictionary;

public class WordList {
	
	private Vector[] words;
	
	private String name;
	
	private Dictionary source;
	
	private String version;
	
	public WordList(Vector[] words, String name, Dictionary source, String version){
		this.words = words;
		this.name = name;
		this.source = source;
		if(version == null || version.equals("")){
			version = "N/A";
		}
		this.version = version;
	}
	
	public void rename(String name){
		this.name = name;
	}
	
	public Vector[] getWords(){
		return words;
	}
	
	public String getName(){
		return name;
	}
	
	public Dictionary getSource(){
		return source;
	}
	
	public String getVersion(){
		return version;
	}

}
