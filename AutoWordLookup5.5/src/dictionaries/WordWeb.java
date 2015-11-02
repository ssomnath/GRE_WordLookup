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
 * This is a WordWeb object capable of retrieving dictioanry
 * and thesaurus entries from the wordweb.com website.
 * 
 * @author Suhas Somnath
 *
 */
public class WordWeb extends WebDictionary implements WebFunctionalities{
	
	/**
	 * Dummy constructor
	 *
	 */
	public WordWeb(){
		
	}
	
	@Override
	public String getName() {
		return "Word Web Online";
	}
	
	@Override
	public Dictionary getDictionaryType() {
		return Dictionary.WORLD_WEB_ONLINE;
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
			String urlpath = "http://www.wordwebonline.com/en";
			    	
			Socket socket = new Socket("www.wordwebonline.com",80);
			
	        socket.setKeepAlive(true);
			
	        /*
	         * Sending the request to the server across the socket.
	         */
	        
	        BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
	        wr.write("GET " + urlpath + "/" + target.toUpperCase() +" HTTP/1.0\r\n");
	        wr.write("Host: " + "www.wordwebonline.com" + "\r\n");
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
	        	if(line.toLowerCase().contains("<br>Perhaps you meant:<blockquote><table>")){
	        		/*
	        		 * Starting a new loop that reads alternate lines
	        		 * containing the possible words:
	        		 */
	        		/*String answer = "";
	        		while ((line = rd.readLine()) != null) {
	        			if(line.toLowerCase().contains("</table></blockquote>")){
	        				System.out.println(answer);
	        				return answer;
	        			}
	        			if(line.toLowerCase().startsWith("<tr><td><b><a href=")){
	        				if(answer.equals("")){
	        					answer = line;
	        				}else{
	        					answer += "\n" + line;
	        				}
	        			}
	        		}
	        		return answer;*/
	        		return null;
	        	}
	        	/*
	        	 * Catching the line:
	        	 */
	        	if(line.contains("<SPAN CLASS=\"head\">")){
	        		line = line.replaceAll("<OL><LI>","");
	        		return line.trim();
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
		if(raw == null){
			DictionaryEntry d = new DictionaryEntry();
			d.word = target;
			d.meanings = new Vector();
			d.meanings.add("No Entry was found");
			Vector v = new Vector();
			v.add(d);
			return v;
		}
		
		/*
		 * placing line breakers every time a new
		 * span tag starts
		 */
		raw = raw.replaceAll("<SPAN","\n<SPAN");
		raw = dictionaries.WebParser.removeTagsOnly(raw,"<A CLASS=\"","</A>");
		/*
		 * Removing the similar sounding words column:
		 */
		int indx = raw.indexOf("<TD class=\"rightSideBar\" VALIGN=\"top\" >");
		raw = raw.substring(0,indx);
		/*
		 * Removing unnecessary tags:
		 */
		raw = raw.replaceAll("</P>","");
		raw = raw.replaceAll("</TD>","");
		raw = raw.replaceAll("<BR>","");
		raw = raw.replaceAll("</OL>","");
		raw = raw.replaceAll("<LI>","");
		raw = raw.replaceAll("&nbsp;","");
		raw = raw.replaceAll("<P class=\"rellnk\">","");
		/*
		 * Deciding if the entry is dictionary / thesaurus type:
		 */
		if(raw.contains("<SPAN CLASS=\"ex\">")){
			return parseThesaurusString(target, raw);
		}else{
			return parseDictionaryString(target, raw);
		}
	}
	
	/**
	 * This method parses a given html tagged line sure to contain
	 * information necessary to make a vector of dictionary objects
	 * 
	 * @param target - String target word.
	 * @param raw - String truncated html line
	 * @return - Vector of DictionaryEntries
	 */
	private Vector<DictionaryEntry> parseDictionaryString(String target, String raw){
		
		if(target == null || raw == null){
			return null;
		}
		/*
		 * Setting up a vector container and a single
		 * dictionaryentry object that is sufficient
		 * to hold all the information
		 */
		Vector<DictionaryEntry> entries = new Vector<DictionaryEntry>();
		DictionaryEntry dict = new DictionaryEntry();
		target = target.trim();
		dict.word = target;
		/*
		 * Splitting the raw line on the basis of line 
		 * breakers or \n charecters
		 */
		raw = raw.trim();
		String[] splits = raw.split("\n");
		/*
		 * For each of the smaller strings, simply look up
		 * the class of the spanning tags and acquire the
		 * information inside the tags to be placed in the
		 * dictionaryEntry object
		 */
		for(int i=0; i<splits.length; i++){
			if(splits[i].contains("<SPAN CLASS=\"def\">")){
				if(dict.meanings == null){
					dict.meanings = new Vector<String>();
				}
				splits[i] = splits[i].substring("<SPAN CLASS=\"def\">".length());
				splits[i] = splits[i].replace("</SPAN>","");
				dict.meanings.add(splits[i].trim());
			}else if(splits[i].contains("<SPAN CLASS=\"seealso\">Type of: </SPAN>")){		
				/*
				 * See also tags are forced as synonyms
				 */
				int last = splits[i].indexOf("</SPAN>");
				splits[i] = splits[i].substring(last + 7);
				splits[i] = splits[i].trim();
				if(dict.synonym.equals("")){
					dict.synonym += splits[i];
				}else{
					dict.synonym += ", " + splits[i];
				}
			}	
		}
		entries.add(dict);
		return entries;
	}
	
	private Vector<ThesaurusEntry> parseThesaurusString(String target, String raw){
		
		/*
		 * multiple thesaurus entries are required
		 * and a vector container is created first.
		 */
		Vector<ThesaurusEntry> entries = new Vector<ThesaurusEntry>();
		ThesaurusEntry current = new ThesaurusEntry();
		target = target.trim();
		current.word = target;
		raw = raw.trim();
		/*
		 * Splicing the raw string at the line breakers
		 * into smaller strings
		 * Placing the information in the respective conainers
		 * depending on the class of the span tags.
		 * 
		 */
		String[] splits = raw.split("\n");
		for(int i=0; i<splits.length; i++){
			if(splits[i].contains("<SPAN CLASS=\"def\">")){
				splits[i] = splits[i].substring("<SPAN CLASS=\"def\">".length());
				splits[i] = splits[i].replace("</SPAN>","");
				current.meaning = splits[i].trim();
			}else if(splits[i].contains("<SPAN CLASS=\"ex\">")){
				splits[i] = splits[i].substring("<SPAN CLASS=\"ex\">".length());
				splits[i] = splits[i].replaceAll("</SPAN>","");
				current.sentence = splits[i].replaceAll("&quot;","");
				/*
				 * A sentence implies the end of a meaning.
				 * Adding the ThesaurusEntry object to the vector
				 * and starting up a new ThesaurusEntry object
				 */
				entries.add(current);
				current = null;
				current = new ThesaurusEntry();
				current.word = target;
			}else if(splits[i].contains("<SPAN CLASS=\"seealso\">Type of: </SPAN>")){		
				int last = splits[i].indexOf("</SPAN>");
				splits[i] = splits[i].substring(last + 7);
				splits[i] = splits[i].trim();
				current.synonym += splits[i];
			}	
		}
		return entries;
	}
	
	public static void main(String[] args){
		WordWeb ww = new WordWeb();
		System.out.println(ww.parseRawEntry("Acriminous",ww.getRawLine("Acriminous")).size());
	}

}

