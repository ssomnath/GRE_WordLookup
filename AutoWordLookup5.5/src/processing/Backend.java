package processing;

import java.util.Vector;

import dictionaries.Dictionary;
import dictionaries.DictionaryManager;
import fileReader.LinearFileReader;
import fileReader.XMLParser;
import fileWriter.GenericFileWriter;
import fileWriter.SerialHtmlWriter;
import fileWriter.TabularHtmlWriter;
import fileWriter.Writer;
import fileWriter.XMLWriter;



/**
 * This is the main back end class capable of taking different input
 * files and finding the meanings, sentences, synonyms etc and generating
 * the output in the format specified.
 * 
 * @author Suhas
 *
 */
public class Backend {
			
private static String version = "5.5";
	
	private static int expectedtime = 0;
	
	public static String getVersion(){
		return version;
	}
	
	public static WordList regenFromXML(String path){
		if(!path.toLowerCase().endsWith(".xml")){
			System.err.println("Backend: Improper use of constructor! \nThis constructor is ONLY used for XML inputs");
			return null;
		}
		
		XMLParser reader = new XMLParser(path);
		return reader.read();
	}
	
	public static WordList lookupOnline(String path, Dictionary dictionary){
		if(!path.toLowerCase().endsWith(".txt")){
			System.err.println("Backend: Improper use of constructor! \nThis constructor is ONLY used for TXT inputs");
			return null;
		}
		
		//First read the file to get the words:
		LinearFileReader reader = new LinearFileReader(path);
		String[] words = reader.getWords();
		
		path = path.substring(0,path.lastIndexOf(".txt"));
		path = path.substring(path.lastIndexOf("\\")+1);
		
		//Now according to the dictionary requested proceed:
		WordFinder wordfinder = new WordFinder(DictionaryManager.getDictionary(dictionary), words);

		Vector[] results = null;
		wordfinder.go();
		try {
			Thread.sleep(expectedtime);
			while((results = wordfinder.getResult()) == null){
				Thread.sleep(2000);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new WordList(results,path,dictionary,version);
	}
		
	public static void writeToFile(WordList list, String path, Writer mode){
		
		GenericFileWriter writer = null;
		
		if(mode == Writer.XML){
			writer = new XMLWriter(path,list);
		}else if(mode == Writer.SERIALHTML){
			writer = new SerialHtmlWriter(path,list);
		}else if(mode == Writer.TABULARHTML){
			writer = new TabularHtmlWriter(path,list);
		}
		writer.write();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WordList wl = Backend.lookupOnline("m_w_problems.txt", Dictionary.MERRIAM_WEBSTER);
		Backend.writeToFile(wl, "backend_test.html", Writer.TABULARHTML);
	}

}
