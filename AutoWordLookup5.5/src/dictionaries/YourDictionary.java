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
 * This is a YourDictionary object capable of retrieving dictioanry
 * and thesaurus entries from the www.yourdictionary.com website.
 * 
 * @author Suhas Somnath
 *
 */
public class YourDictionary extends WebDictionary implements WebFunctionalities{
	
	/**
	 * Dummy constructor
	 *
	 */
	public YourDictionary(){
		
	}
	
	@Override
	public String getName() {
		return "Your Dictionary";
	}
	
	@Override
	public Dictionary getDictionaryType() {
		return null;
	}
	
	/**
	 * This method implements the same in the WebDictionary
	 * interface. It returns a vector of ThesaurusEntries
	 * after looking them up.
	 * @param target - String target word
	 * @return Vector of ThesaurusEntry objects
	 */
	public Vector getThesaurusEntries(String target){
		if(target == null){
			return null;
		}
		target = target.trim();
		if(target.equals("")){
			return null;
		}
		
		/*
		 * Searching for thesaurus entry:
		 */
		String thes =  getRawLine(target);
		Vector entries = parseRawEntry(target, thes);
		return entries;
	}
	
	/**
	 * This method implements the same in the WebDictionary
	 * interface. It returns a vector of DictioanryEntries
	 * after looking them up.
	 * @param target - String target word
	 * @return Vector of DictionaryEntry objects
	 */
	public Vector getDictionaryEntries(String target){
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
		Vector entries = parseRawEntry(target, dict);
		return entries;
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
			String urlpath = "http://www.yourdictionary.com/";
			    	
			Socket socket = new Socket("www.yourdictionary.com",80);
			
	        socket.setKeepAlive(true);
			
	        /*
	         * Sending the request to the server across the socket.
	         */
	        
	        BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
	        wr.write("GET " + urlpath + target + " HTTP/1.0\r\n");
	        wr.write("Host: " + "www.yourdictionary.com" + "\r\n");
	        wr.write("Connection: Close\r\n");
	        wr.write("\r\n");
	        wr.flush();
	        
	        /*
	         * Reading first few lines before defenition starts:
	         */

	        BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        String line;
	        while ((line = rd.readLine()) != null) {
	        	/*
	        	 * Failure case:
	        	 */
	        	
	        	if(line.contains("The server returned a 404 response")){
	        		return null;
	        	}
	        	
	        	/*
	        	 * Catching the line:
	        	 */
	        	if(line.contains("<ol class=\"sense\">")){
	        		line = line.trim();
	        		
	        		return line;
	        		
	        	}
	        }
	        
	        /*
	         * Closing reading and writing streams
	         */
	        wr.close();
	        rd.close();  
	        
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
	 * This method parses the given html tagged line from 
	 * www.wordweb.com to remove all the unnecessary tags and
	 * return a vector of either thesaurus of dictionaryentry
	 * objects
	 * 
	 * @param target - String target word
	 * @param raw - String raw html tagged line
	 * @return - Vector of thesaurus / dictionary objects
	 */
	private Vector parseRawEntry(String target, String raw){
		
		if(target == null){
			return null;
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
	private Vector parseDictionaryString(String target, String raw){
		
		if(target == null || raw == null){
			return null;
		}
		/*
		 * Setting up a vector container and a single
		 * dictionaryentry object that is sufficient
		 * to hold all the information
		 */
		Vector entries = new Vector();
		DictionaryEntry dict = new DictionaryEntry();
		target = target.trim();
		dict.word = target;
		
		return null;
	}
	
	private Vector parseThesaurusString(String target, String raw){
		
		return null;
	}
	
	public static void main(String[] args){
		YourDictionary ww = new YourDictionary();
		System.out.println(ww.getRawLine("rude"));
		//System.out.println(ww.parseRawEntry("Acriminous",ww.getRawLine("Acriminous")).size());
	}

}


