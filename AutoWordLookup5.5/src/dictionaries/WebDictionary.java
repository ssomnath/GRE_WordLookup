package dictionaries;

import java.util.Vector;

import definitions.DictionaryEntry;
import definitions.ThesaurusEntry;

/**
 * This is a dummy class acting as a general object
 * type for any class that retrieves thesaurus and
 * dictionary entry objects.
 * 
 * @author Suhas Somnath
 *
 */
abstract public class WebDictionary {
	
	/**
	 * Dummy method supposedly takes target string
	 * and returns null
	 * @param target
	 * @return Vector of Thesaurus Entries
	 */
	public Vector<ThesaurusEntry> getThesaurusEntries(String target){
		return null;
	}
	
	/**
	 * Dummy method takes string target and returns 
	 * a vector of dictionary entries.
	 * @param target
	 * @return Vector of Dictioanry Entries
	 */
	public Vector<DictionaryEntry> getDictionaryEntries(String target){
		return null;
	}
	
	public abstract String getName();
	
	public abstract Dictionary getDictionaryType();
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*WebDictionary wd = new WordWeb();
		Vector v = wd.getThesaurusEntries("livid");
		System.out.println(v.size());*/
		
		/*WebDictionary wd = new MerriamWebster();
		Vector v = wd.getDictionaryEntries("annote");
		System.out.println(v.size());*/
	}

}

