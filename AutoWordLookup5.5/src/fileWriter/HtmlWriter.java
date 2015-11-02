package fileWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import processing.WordList;

import definitions.DictionaryEntry;
import definitions.ThesaurusEntry;
import dictionaries.DictionaryManager;


public abstract class HtmlWriter extends GenericFileWriter{
	
	/**
	 * Constructor - takes the absolute path of the output html
	 * file.
	 * 
	 * @param outputPath - absolute path of the output html file.
	 */
	public HtmlWriter(String outputPath, WordList wl){
		super(outputPath, wl);
	}
	
	/**
	 * This method takes a generic Vector that may contain either
	 * Thesaurus / Dictionary Entries and returning a string of
	 * html snippet format for that particular target word
	 * @param entry - Vector either containing thesaurus / Dictionary Entries
	 * @return - HTML format snippet string
	 */
	public String getHtml(Vector entry){
		if(entry == null){
			return "";
		}else if(entry.size() == 0){
			return "";
		}
		Object o = entry.get(0);
		if(o == null){
			return "";
		}
		if(o.getClass().getSimpleName().equals("ThesaurusEntry")){
			return thesVectorToHtml(entry);
		}else if(o.getClass().getSimpleName().equals("DictionaryEntry")){
			return dictVectorToHtml(entry);
		}
		return "";
	}
	
	public abstract String thesVectorToHtml(Vector<ThesaurusEntry> vect);
	
	public abstract String dictVectorToHtml(Vector<DictionaryEntry> vect);
	
	/**
	 * 
	 * @param target - Target word
	 * @param line - Sentence containing the target
	 * @return - Sentence with target italicized
	 */
	public String italicizeTarget(String target, String line){
		if(target == null || line == null){
			return "";
		}
		target = target.toLowerCase();
		line = line.replaceAll(target,"<I>"+target+"</I>");
		return line;
	}
	
	/**
	 * This is the main method which writes the data to the html file
	 *
	 * @param entries - Vector array of thesaurus / dictionary entries
	 */
	public void writeToFile(String output){
		
		File f = new File(filepath);
				
		int indx = filepath.lastIndexOf("\\");
		String header = filepath.substring(indx+1);
		header = header.substring(0,header.length()-5) + " - Auto Word Lookup";

		try {
			FileWriter fw = new FileWriter(f);
			String prev = "<html>\n<head>\n<title>"+header+"</title>\n";
			prev += "<center><H1>"+ header +"</H1></center>";
			prev += "</head>\n<body>\n";
			String website = DictionaryManager.getDictionary(this.wordlist.getSource()).getName();
			prev += "<strong>Thesaurus Used:</strong> " + website + "\n<BR>";
			prev += "<strong>Version:</strong> " + this.wordlist.getVersion() + "\n<BR>";
			prev += "<strong>Author:</strong> Suhas Somnath<br>\n<HR>\n";
			String end = "\n</body>\n</html>\n";
			output = prev + getStyleString() + output + end;
			fw.write(output);
			fw.close();
			
			System.out.println("Please lookup written file: " + filepath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public abstract String getContent(Vector[] entries);
	
	abstract String getStyleString();
	
	public void write(){
		super.write();
		
		writeToFile(getContent(wordlist.getWords()));
		
		signalCompleted();
	}
}
