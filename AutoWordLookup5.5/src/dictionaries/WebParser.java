package dictionaries;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebParser {
	
	/**
	 * Method responsible for removing tags from a string that may
	 * or may not contain them.
	 * @param raw -  html tagged string
	 * @param start - starting tag
	 * @param end - ending tag
	 * @return - same line without anchors
	 */
	public static String removeTagsOnly(String raw, String start, String end){
		
		if(raw == null){ 
			return "";
		}
		if(start == null || end == null){
			return raw;
		}
		raw = raw.trim();
		start = start.trim();
		end = end.trim();
		if(raw.equals("") || start.equals("") || end.equals("")){
			return raw;
		}
		
		raw = raw.replaceAll(end,"");
		while(raw.contains(start)){
			int begin = raw.indexOf(start);
			String temp = raw.substring(0,begin);
			raw = raw.substring(begin);
			int end1 = raw.indexOf(">");		
			raw = raw.substring(end1+1);
			raw = temp + " " + raw;			
		}
		return raw;
	}
	
	/**
	 * Separates the raw string into two parts - the part with the tag and its contents
	 * only and the rest of the string. Removes the tags and returns the remaining raw
	 * string and the contents within the tags. NOTE - nesting is NOT accounted for
	 * @param raw
	 * @param start
	 * @param end
	 * @return
	 */
	public static String[] getInsideTag(String raw, String start, String end){
		
		if(raw == null){ 
			return null;
		}
		if(start == null || end == null){
			return null;
		}
		raw = raw.trim();
		start = start.trim();
		end = end.trim();
		if(raw.equals("") || start.equals("") || end.equals("")){
			return null;
		}
		
		int beginstart = raw.indexOf(start);
		
		int beginend = raw.indexOf(end);
		
		if(beginstart<0 || beginend < 0){
			return null;
		}
		
		String left = raw.substring(0,beginstart);
		String center = raw.substring((beginstart+start.length()),beginend);
		String right = raw.substring(beginend+end.length());
		
		String[] ret = {center, (left+right)};
		
		return ret;	
	}
	
	/**
	 * Uses regular expressions to remove the first set of given tags and 
	 * everything contained within the tags
	 * 
	 * @param raw - String with tags
	 * @param start - starting tag
	 * @param end - ending tag
	 * @return string with those particular tags and inner contents removed
	 */
	public static String removeFirstTagPlus(String raw, String start, String end){
		
		if(raw == null){ 
			return "";
		}
		if(start == null || end == null){
			return raw;
		}
		raw = raw.trim();
		start = start.trim();
		end = end.trim();
		if(raw.equals("") || start.equals("") || end.equals("")){
			return raw;
		}
		
		String target = start + ".+?" + end;
		
		Pattern pattern = Pattern.compile(target);
		Matcher matcher = pattern.matcher(raw);
		raw = matcher.replaceFirst("");
		
		return raw;
	}
	
	/**
	 * Uses regular expressions to remove the given tags and everything contained 
	 * within the tags
	 * 
	 * @param raw - String with tags
	 * @param start - starting tag
	 * @param end - ending tag
	 * @return string with those particular tags and inner contents removed
	 */
	public static String removeAllTagsPlus(String raw, String start, String end){
		
		if(raw == null){ 
			return "";
		}
		if(start == null || end == null){
			return raw;
		}
		raw = raw.trim();
		start = start.trim();
		end = end.trim();
		if(raw.equals("") || start.equals("") || end.equals("")){
			return raw;
		}
		
		String target = start + ".+?" + end;
		
		Pattern pattern = Pattern.compile(target);
		Matcher matcher = pattern.matcher(raw);
		raw = matcher.replaceAll("");

		return raw;
	}

	public static void main(String[] args){
		
		String[] ret = getInsideTag("first<b>second</b>third","<b>","</b>");
		System.out.println(ret[0]);
		System.out.println(ret[1]);
	}
}
