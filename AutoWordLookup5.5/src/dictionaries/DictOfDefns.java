package dictionaries;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import definitions.DictionaryEntry;
import definitions.ThesaurusEntry;


/**
 * This is a DictOfDefns object capable of retrieving dictionary
 * and thesaurus entries from the DictionaryOfDefinitions.com website.
 * 
 * @author Suhas Somnath
 *
 */
public class DictOfDefns extends WebDictionary implements WebFunctionalities{
	
	/**
	 * Dummy constructor
	 *
	 */
	public DictOfDefns(){
		
	}
	
	public String getName(){
		return "Dictionary Of Definitions";
	}
	
	public Dictionary getDictionaryType(){
		return Dictionary.DICT_OF_DEFNS;
	}
	
	/**
	 * This dictionary does NOT Support thesaurus entries
	 * By default, it will return null
	 * 
	 * @param target - String target word
	 * @return Vector of ThesaurusEntry objects
	 */
	public Vector<ThesaurusEntry> getThesaurusEntries(String target){
		return null;
	}
	
	/**
	 * This method implements the same in the WebDictionary
	 * interface. It returns a vector of DictioanryEntries
	 * after looking them up.
	 * @param target - String target word
	 * @return Vector of DictionaryEntry objects
	 */
	public Vector<DictionaryEntry> getDictionaryEntries(String target){
		if(target == null){
			return null;
		}
		target = target.trim();
		if(target.equals("")){
			return null;
		}
		/*
		 * Acquiring the dictionary entry:
		 */
		String dict = getRawLine(target);	
		DictionaryEntry entry = parseDictionaryString(target, dict);
		Vector<DictionaryEntry> v = new Vector<DictionaryEntry>();
		v.add(entry);
		return v;
	}
	
	/**
	 * This method simply fetches the CSS HTML tagged line from
	 * the website containing all the necessary information about
	 * the target word.
	 * 
	 * @param target - String word
	 * @return - String html line containing information
	 */
	private String getRawLine(String target){
		try{
			String urlpath = "http://www.dictionaryofdefinitions.com/what-is-the-definition-of-";
			    	
			Socket socket = new Socket("www.dictionaryofdefinitions.com",80);
			
	        socket.setKeepAlive(true);
			
	        /*
	         * Sending the request to the server across the socket.
	         */
	        
	        BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
	        wr.write("GET " + urlpath + target + ".html HTTP/1.0\r\n");
	        wr.write("Host: " + "www.dictionaryofdefinitions.com" + "\r\n");
	        wr.write("Connection: Close\r\n");
	        wr.write("\r\n");
	        wr.flush();
	        
	        /*
	         * Reading first few lines before defenition starts:
	         */

	        BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        String line;
	        String answer = "";
	        while ((line = rd.readLine()) != null) {
	        	/*
	        	 * Failure case:
	        	 */
	        	if(line.toLowerCase().contains("<b>1. not sure of the spelling?</b>")){
	        		return null;
	        	}
	        	/*
	        	 * Catching the lines:
	        	 */
	        	if(line.contains("<span class=\"num1\">") || line.contains("<span class=\"mini\">")){
	        		answer += "\n" + line;
	        	}
	        	/*
	        	 * Little more complicated with synonyms
	        	 */
	        	if(line.contains("<strong>Synonym Dictionary</strong>")){
	        		answer += "\n" + line;
	        		while ((line = rd.readLine()) != null) {
	        			if(line.startsWith("<a href=\"")){
	        				answer += "\n" + line;
	        			}else{
	        				break;
	        			}
	        		}
	        	}	
	        }
	        
	        /*
	         * Closing reading and writing streams
	         */
	        wr.close();
	        rd.close();  
	        
	        return answer;
	        
		}catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * This method parses a given html tagged line sure to contain
	 * information necessary to make a vector of dictionary objects
	 * 
	 * @param target - String target word.
	 * @param raw - String truncated html line
	 * @return - Vector of DictionaryEntries
	 */
	private DictionaryEntry parseDictionaryString(String target, String raw){
		
		if(target == null || raw == null){
			return null;
		}
		/*
		 * Setting up a vector container and a single
		 * dictionaryentry object that is sufficient
		 * to hold all the information
		 */
		DictionaryEntry dict = new DictionaryEntry();
		target = target.trim();
		dict.word = target;
		
		raw = raw.replaceAll("<br/>","");
		raw = raw.replaceAll("<br />","");
		raw = raw.replaceAll("&quot;","");
		raw = raw.replaceAll("<li>","");
		raw = raw.replaceAll("&#183;&#183;", "");
		raw = dictionaries.WebParser.removeTagsOnly(raw, "<a href=", "</a>");
		
		String[] splits = raw.split("\n");
		
		if(splits.length <= 1){
			return null;
		}
		
		dict.meanings = new Vector<String>();
		dict.sentence = new Vector<String>();
		
		for(int i=0; i<splits.length; i++){
			if(splits[i].contains("num1")){
				String[] temp = dictionaries.WebParser.getInsideTag(splits[i], "<span class=\"num1\">", "</span>");
				if(temp == null){
					continue;
				}
				dict.meanings.add(temp[1]);
			}else if(splits[i].contains("mini")){
				String[] temp = dictionaries.WebParser.getInsideTag(splits[i], "<span class=\"mini\">", "</span>");
				if(temp == null){
					continue;
				}
				dict.sentence.add(temp[0]);
			}else if(splits[i].contains("Synonym Dictionary")){
				raw = "";
				for(int j=i+1; j< splits.length; j++){
					raw += splits[j].trim() + " ";
				}
				dict.synonym = raw;
			}
			
		}
		return dict;
	}
	
	public static void main(String[] args){
		DictOfDefns ww = new DictOfDefns();
		String target = "rude";
		String raw = ww.getRawLine(target);
		//System.out.println(raw);
		DictionaryEntry d = ww.parseDictionaryString(target, raw);	
		d.print();	
	}

}


