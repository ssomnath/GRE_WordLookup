package definitions;

import java.util.Vector;

/**
 * Ancillary class that helps in easy storage of a dictionary
 * entry that was obtained and parsed. Defenitions are stored as
 * a vector of strings.
 * @author Suhas
 */
public class DictionaryEntry{
	public String word;
	public Vector<String> meanings;
	public Vector<String> sentence;
	public String synonym = "";
	
	public void print(){
		System.out.println("*************************************************");
		System.out.println("Dictionary Entry: \nWord: " + this.word);
		if(this.meanings != null){
			System.out.println("Meanings: ");
			for(int i=0; i<this.meanings.size(); i++){
				System.out.println("\t" + meanings.get(i));
			}
		}
		
		if(this.sentence != null){
			System.out.println("Sentences: ");
			for(int i=0; i<this.sentence.size(); i++){
				System.out.println("\t\"" + sentence.get(i) + "\"");
			}
		}
		if(!synonym.equals("")){
			System.out.println("Synonyms: " + this.synonym);
		}
		System.out.println("*************************************************");
	}
}