package fileWriter;

import java.util.Vector;

import processing.WordList;

public abstract class GenericFileWriter {

	String filepath;
	
	WordList wordlist;
	
	int progress = 95;
	
	/**
	 * Constructor - takes the absolute path of the output html
	 * file.
	 * 
	 * @param outputPath - absolute path of the output html file.
	 */
	public GenericFileWriter(String outputPath, WordList wl){
		this.filepath = outputPath;
		this.wordlist = wl;
	}
	
	public void write(){
		
		/*
		String suffix = "";
		if(version != null){
			suffix = this.version;
		}
		if(this.website != null){
			if(!suffix.equals("")){
				suffix += "_" + this.website;
			}else{
				suffix = this.website;
			}
		}
		
		if(filepath.endsWith(".xml") || filepath.endsWith(".html")){
			filepath = filepath.substring(0,filepath.indexOf("."));
		}
		
		filepath += "_" + suffix;
		//the filewriter has to only tack along the extension to the prepared file name
		*/
	}
	
	public int getProgress(){
		return progress;
	}
	
	void signalCompleted(){
		progress = 100;
	}
	
}
