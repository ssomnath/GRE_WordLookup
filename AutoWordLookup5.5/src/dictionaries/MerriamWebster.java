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
 * This is the main class responsible for acquiring the thesaurus entry
 * of a single word from the MerriamWebster online. 
 * If such a thesuarus entry were not 
 * found, it acquires the dictionary entry of the same. 
 * 
 * @author Suhas
 *
 */
public class MerriamWebster extends WebDictionary implements WebFunctionalities{
	
	/**
	 * Dummy constructor
	 */
	public MerriamWebster(){		
	}
	
	@Override
	public String getName() {
		return "Merriam-Webster";
	}
	
	@Override
	public Dictionary getDictionaryType() {
		return Dictionary.MERRIAM_WEBSTER;
	}
	
	/**
	 * This method implements the same in the WebDictionary
	 * interface. It returns a vector of ThesaurusEntries
	 * after looking them up.
	 * @param target - String target word
	 * @return Vector of ThesaurusEntry objects
	 */
	public Vector<ThesaurusEntry> getThesaurusEntries(String target){
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
		String thes =  getRawThesEntry(target);
		Vector<ThesaurusEntry> entries = parseThesEntry(target, thes);
		return entries;
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
		String dict = getRawDictEntry(target);
		Vector<DictionaryEntry> entries = new Vector<DictionaryEntry>(1);
		entries.add(parseDictEntry(target, dict));
		return entries;
	}
		
	/**
	 * This method is responsible for getting the raw html tagged text
	 * that was acquired online pertaining to the supplied target.
	 * @param target - Single word;
	 * @return - String - raw html tagged.
	 */
	private String getRawDictEntry(String target){
		try{
			String urlpath = "/dictionary";
			    	
			Socket socket = new Socket("www.merriam-webster.com",80);
			
	        socket.setKeepAlive(true);
			
	        /*
	         * Sending the request to the server across the socket.
	         * http://www.rexswain.com/httpview.html
	         */
	        
	        BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
	        wr.write("GET " + urlpath + "/" + target +" HTTP/1.0\r\n");
	        wr.write("Host: " + "www.merriam-webster.com" + "\r\n");
	        wr.write("Connection: Close\r\n");
	        wr.write("\r\n");
	        wr.flush();
	        
	        /*
	         * Reading first few lines before defenition starts:
	         */

	        BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        String line;
	        String answer="";
	        int indx = 1;
	        while ((line = rd.readLine()) != null) {
	        	/*
	        	 * Failure case:
	        	 */
	        	if(line.toLowerCase().contains("suggestions for <strong>"+target.toLowerCase()+"</strong>:")){
	        		while ((line = rd.readLine()) != null) {
	        			if(line.toLowerCase().contains("</pre>")){
	        				break;
	        			}
	        			if(answer.equals("")){
	        				answer = line;
	        			}else{
	        				answer += "\n" + line;
	        			}
	        		}
	        		wr.close();
	     	        rd.close(); 
	        		return answer;
	        	}
	        	
	        	/*
	        	 * Success case:
	        	 */
	        	if((indx = line.indexOf("<p class=\"d\">")) >=0){
	        		// Remove all the text before the start of definition
	        		line = line.substring(indx);
	        		// Remove all the class="r" text
	        		line = removeClassR(line);
	        		// Remove the last few words of the text:
	        		line = line.replaceAll("</div><script type=\"text/javascript\">", "");
	        		return line;
	        	}
	        }
	        
	        /*
	         * Closing reading and writing streams
	         */
	        wr.close();
	        rd.close();  
	        
	        if(answer.equals("")){return null;}
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
	
	private String removeClassR(String line){
		if(line == null){
			return null;
		}
		line = line.trim();
		if(line.equals("")){
			return null;
		}
		// There could be more than one set of class=r tags:
		int indx = 1;
		while((indx = line.indexOf("<p class=\"r\">")) >=0){
			String first = line.substring(0,indx);
			String temp = line.substring(indx);
			int end = temp.indexOf("</p>");
			if(end < 0){
				//something wrong with given string
				return line;
			}
			String last = temp.substring(end+4);
			line = first + last;
		}
		return line;
	}

	/**
	 * This method is responsible for getting the raw html tagged thesaurus entry
	 * that was acquired online pertaining to the supplied target.
	 * @param target - Single word;
	 * @return - String - raw html tagged.
	 */
	private String getRawThesEntry(String target){
		try{
			String urlpath = "/thesaurus";
			    	
			Socket socket = new Socket("www.merriam-webster.com",80);
			
	        socket.setKeepAlive(true);
			
	        /*
	         * Sending the request to the server across the socket.
	         * http://www.rexswain.com/httpview.html
	         */
	        
	        BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
	        wr.write("GET " + urlpath + "/" + target +" HTTP/1.0\r\n");
	        wr.write("Host: " + "www.merriam-webster.com" + "\r\n");
	        wr.write("Connection: Close\r\n");
	        wr.write("\r\n");
	        wr.flush();
	        
	        /*
	         * Reading the response from the server:
	         */

	        BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        String line;
	        while ((line = rd.readLine()) != null) {
	        	/*
	        	 * Failure case:
	        	 */
	        	if(line.contains("The word you've entered isn't in the thesaurus")){
	        		wr.close();
	     	        rd.close(); 
	        		return null;
	        	}
	        	/*
	        	 * Success case:
	        	 */
	        	int indx = -1;
	        	if((indx = line.indexOf("<div class=\"d\">")) >=0){
	        		// Remove all the text before the start of definition
	        		line = line.substring(indx);
	        		return line;
	        	}
	        }
	        /*
	         * Closing reading and writing streams
	         */
	        wr.close();
	        rd.close();  

	        return null;
	        
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
	 * This is the main method that removes the html tags present in the raw
	 * result that was obtained from the getThesaurusEntry method. It returns 
	 * a vector of ThesaurusEntry objects with the information in the given
	 * thesaurus string.
	 * @param target - Target word
	 * @param raw - Resulting raw string from the website
	 * @return - Vector of thesaurusEntry objects 
	 */
	private Vector<ThesaurusEntry> parseThesEntry(String target, String raw){
		if(target == null || raw == null){
			return null;
		}
		/*
		 * Creating vector to hold thesaurusEntry objects
		 */
		target = target.trim();
		raw = raw.trim();
		Vector<ThesaurusEntry> entries = new Vector<ThesaurusEntry>();
		/*
		 * Replacing CSS italics tags with html ones
		 */
		raw = raw.replaceAll("<em>","");
		raw = raw.replaceAll("<em/>","");
		raw = raw.replaceAll("</em>","");
		raw = raw.replaceAll("&lt;","");
		raw = raw.replaceAll("&gt;","");
		raw = raw.replaceAll("<strong>:</strong>","");
		raw = raw.replaceAll("<em class=\"unicode\">","");
		raw = raw.replaceAll("<span class=\"dxn\">.</span>","");
		raw = raw.replaceAll("<br/>","");
		raw = raw.replaceAll("<div class=\"d\">","");
		raw = raw.replaceAll("<!--INFOLINKS_OFF-->","");
		raw = raw.replaceAll("<!--INFOLINKS_ON-->","");
		raw = raw.replaceAll("Meaning:","");
		
		raw = raw.replaceAll("<span class=\"unicode\">&#8221;</span>",".");
		raw = raw.replaceAll("<span class=\"unicode\">&#8220;</span>","");
		
		/*
		 * Idea is to separate the multiple possible thesaurus entries first
		 * and then parse them separately each
		 */
		raw = raw.trim();
		String[] splits = raw.split("<strong>.</strong>");
		
		for(int i=0; i< splits.length; i++){
			ThesaurusEntry ret = parseSingleThesaurusEntry(target, splits[i]);
			if(ret!= null){
				entries.add(ret);
			}
		}
		return entries;
	}
	
	private ThesaurusEntry parseSingleThesaurusEntry(String target, String raw){
		if(target == null || raw == null){
			return null;
		}
		target = target.trim();
		raw = raw.trim();
		if(raw.length() == 0 || target.length() == 0){
			return null;
		}
		ThesaurusEntry answer = new ThesaurusEntry();
		answer.word = target;
		int see = raw.indexOf("see <a href=");
		
		if(see >= 0){
			// The simpler one.
			String last = raw.substring(raw.indexOf("<a href="));
			String first = raw.substring(0,see);
			last = last.replace("<div>", "");
			last = last.replace("</div>", "");
			last = last.replace("<div/>", "");
			last = WebParser.removeTagsOnly(last, "<a href=", "</a>");
			last = last.replaceAll("<!--word_definition-->", "");
			answer.synonym = last.trim();
			raw = first;
			
		}else{
			// This is the longer entry having synonyms, antonyms etc.
			// Easier to first extract all the synonyms, and then the sentences
			
			String[] splits = WebParser.getInsideTag(raw, "<div><strong>Synonyms</strong>", "</div>");
			if(splits != null){
				answer.synonym = splits[0].trim();
				raw = splits[1];
			}
			
			splits = WebParser.getInsideTag(raw, "<div><strong>Related Words</strong>", "</div>");
			if(splits != null){
				answer.relword = splits[0].trim();
				raw = splits[1];
			}
			
			splits = WebParser.getInsideTag(raw, "<div><strong>Near Antonyms</strong>", "</div>");
			if(splits != null){
				answer.nearant = splits[0].trim();
				raw = splits[1];
			}
			
			splits = WebParser.getInsideTag(raw, "<div><strong>Antonyms</strong>", "</div>");
			if(splits != null){
				answer.antonym = splits[0].trim();
				raw = splits[1];
			}
		}
		
		// Regardless of the harder / easier one, the meaning / sentence structure is the same:
		
		// removing any garbage after the </span> tag:
		raw = raw.substring(0,raw.indexOf("</span>")+8);
		String[] splits = WebParser.getInsideTag(raw, "<span class=\"vi\">", "</span>");
		if(splits == null){
			// Case when its a garbage starting / ending raw string
			return null;
		}
		answer.sentence = splits[0].trim();
		int indx = splits[1].indexOf("</div>");
		if(indx > 0){
			splits[1] = splits[1].substring(0,indx);
		}
		answer.meaning = splits[1].trim();
		
		return answer;
	}
	
	/**
	 * This method is responsible for taking the raw string from the web
	 * containing the dictionary entry and turning it into a vector of
	 * DictionaryEntry objects
	 * 
	 * @param target - target word
	 * @param raw - raw html string
	 * @return - Vector of DictionaryEntry objects
	 */
	private DictionaryEntry parseDictEntry(String target, String raw){
		
		if(target == null){
			return null;
		}
		
		DictionaryEntry d = new DictionaryEntry();
		d.word = target;
		d.meanings = new Vector<String>();
		
		if(raw == null){
			/*
			 * In case even a dictionary entry wasnt found,
			 * a dictioanry entry is returned which says that 
			 * no entries could be found whatsoever.
			 */
			d.meanings.add("No entries Found");
			return d;
		}
		/*
		 * Trash removal:
		 */
		raw = raw.replaceAll("<!--INFOLINKS_OFF-->","");
		raw = raw.replaceAll("<!--INFOLINKS_ON-->","");
		
		int butind = raw.indexOf("<input");
		while(butind > 0){
			String left = raw.substring(0,butind);
			String right = raw.substring(butind);
			butind = right.indexOf(">");
			right = right.substring(butind+1);
			raw = left + right;
			butind = raw.indexOf("<intput");
		}
		raw = WebParser.removeAllTagsPlus(raw, "<span class=\"unicode\">", "</span>");
		raw = WebParser.removeAllTagsPlus(raw, "<span class=\"pr\">", "</span>");
		raw = raw.replaceAll("<em>", "");
		raw = raw.replaceAll("</em>", "");
		raw = raw.replaceAll("<sup>", "");
		raw = raw.replaceAll("</sup>", "");
		raw = raw.replaceAll("<sub>", "");
		raw = raw.replaceAll("</sub>", "");
		raw = WebParser.removeTagsOnly(raw, "<a href", "</a>");
		raw = raw.trim();
		target = target.trim();
		if(raw.equals("")){
			/*
			 * In case even a dictionary entry wasnt found,
			 * a dictioanry entry is returned which says that 
			 * no entries could be found whatsoever.
			 */
			d.meanings.add("No entries Found");
			return d;
		}
		
		// Remove the Synonyms first (if any)
		
		int indx = 1;
		String temp = "<div><strong>synonyms</strong>";
		if((indx = raw.indexOf(temp)) >= 0){
			d.synonym = raw.substring(indx + temp.length()+1);
			//Generally accepted that the synonym occurs at the end of the raw line
			// cut the raw string to remove this last bit:
			raw = raw.substring(0,indx);
			
			d.synonym = d.synonym.replace("see ", "");
			
			d.synonym = d.synonym.replace("</div>", "");
			d.synonym = d.synonym.replace("</p>", "");
			// Synonyms should generally be loaded with lots of anchors:
			d.synonym = dictionaries.WebParser.removeTagsOnly(d.synonym,"<a href","</a>");
			d.synonym = d.synonym.trim();
		}
		
		// Start tearing down the meanings and sentences:
		
		raw = dictionaries.WebParser.removeTagsOnly(raw,"<a href","</a>");
		raw = raw.replaceAll("<em>","");
		raw = raw.replaceAll("</em>","");
		raw = raw.replaceAll("<em/>","");
		raw = raw.replaceAll("<p class=\"d\">","");
		
		
		// Ripping apart the senetences only:
		
		d.sentence = new Vector<String>();
		
		
		String[] splits = WebParser.getInsideTag(raw, "<span class=\"vi\">&lt;", "&gt;</span>");
		while(splits != null){
			raw = splits[1];
			d.sentence.add(splits[0]);
			splits = WebParser.getInsideTag(raw, "<span class=\"vi\">&lt;", "&gt;</span>");
		}
		
		// strategy - consolidate all "a", "b",..... into one meaning.
		
		raw = raw.replaceAll("</p>", "");
		raw = raw.replaceAll("<div>", "");
		raw = raw.replaceAll("</div>", "");
		
		splits = raw.split("<em class=\"v\">");
		
		for(int i=0; i<splits.length; i++){
			int garb = splits[i].indexOf("<strong>");
			if(garb < 0){
				continue;
			}
			
			splits[i] = splits[i].substring(garb);
			
			splits[i] = splits[i].replaceAll("<strong>:</strong>","");
			
			String[] splits2 = splits[i].split("<br/>");
			
			for(int j=0; j< splits2.length; j++){
				int indx2 = splits2[j].indexOf("</strong>");
				if(indx2 >= 0){
					splits2[j] = splits2[j].substring(indx2+"</strong>".length());
				}
				
				d.meanings.add(WebParser.removeAllTagsPlus(splits2[j], "<strong>", "</strong>").trim());
			}
		}
		return d;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MerriamWebster wt = new MerriamWebster();
		/*Vector temp = wt.getThesaurusEntries("clamor");
		for(int i=0; i< temp.size(); i++){
			ThesaurusEntry e = (ThesaurusEntry) temp.get(i);
			e.print();
		}*/
		//Vector v = wt.parseThesEntry("abet",wt.getRawThesEntry("abet"));//two entries with "see"
		//Vector v = wt.parseThesEntry("vociferous",wt.getRawThesEntry("vociferous"));//one large entry with "syn"
		DictionaryEntry d = wt.parseDictEntry("mutiny", wt.getRawDictEntry("sycamore"));
		d.print();
	}
}

