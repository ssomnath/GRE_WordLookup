package dictionaries;

/**
 * This is the only class in the entire project
 * that is capable of telling the rest of the
 * code about the different dictionaries available
 * Each time a new dictionary is added - a new
 * object of that type has to be added to the
 * array.
 */
public class DictionaryManager {
	
	private static WebDictionary[] dictionaries = {
			new MerriamWebster(),
			new WordWeb(),
			new DictOfDefns()
	};
	
	/**
	 * This method returns a list of all
	 * available dictionary items.
	 * @return
	 */
	public static Dictionary[] getAllDictionaries(){
		
		Dictionary[] dict = new Dictionary[dictionaries.length];
		for(int i=0; i< dictionaries.length; i++){
			dict[i] = dictionaries[i].getDictionaryType();
		}
		return dict;
	}
	
	/**
	 * Method was written to remove the need to modify any other packages
	 * when a new dictionary was added.
	 * @param dictionary - Dictionary name
	 * @return - Corresponding dictionary object
	 */
	public static WebDictionary getDictionary(Dictionary dictionary){
		for(int i=0; i< dictionaries.length; i++){
			if(dictionaries[i].getDictionaryType() == dictionary){
				return dictionaries[i];
			}
		}
		return null;
	}
	
	/**
	 * Returns a String array of all fully functional dictionaries
	 * @return
	 */
	public static String[] getAllDictionaryTitles(){
		String[] dicts = new String[dictionaries.length];
		for(int i=0; i< dictionaries.length; i++){
			dicts[i] = dictionaries[i].getName();
		}
		return dicts;
	}
	
	public static Dictionary getDictionaryType(String name){
		if(name == null){
			return dictionaries[0].getDictionaryType();
		}
		name = name.trim();
		if(name.equals("")){
			return dictionaries[0].getDictionaryType();
		}
		for(int i=0; i< dictionaries.length; i++){
			if(name.equals(dictionaries[i].getName())){
				return dictionaries[i].getDictionaryType();
			}
		}
		//Default
		return dictionaries[0].getDictionaryType();
	}
}
