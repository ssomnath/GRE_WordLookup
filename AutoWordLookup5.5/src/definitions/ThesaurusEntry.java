package definitions;

/**
 * Ancillary class that helps in easy storage of a thesaurus
 * entry that was obtained and parsed.
 * @author Suhas
 */
public class ThesaurusEntry{
	public String word;
	public String meaning;
	public String sentence;
	public String synonym;
	public String antonym;
	public String nearant;
	public String relword;
	
	public void print(){
		System.out.println("*************************************************");
		System.out.println("Thesaurus Entry: \nWord: " + this.word);
		if(!(this.sentence == null) && !this.sentence.equals("")){
			System.out.println("Sentence: \"" + this.sentence + "\"");
		}
		if(!(this.synonym == null) && !this.synonym.equals("")){
			System.out.println("Synonyms: \"" + this.synonym + "\"");
		}
		if(!(this.relword == null) && !this.relword.equals("")){
			System.out.println("Related Words: \"" + this.relword + "\"");
		}
		if(!(this.nearant == null) && !this.nearant.equals("")){
			System.out.println("Near Antonyms: \"" + this.nearant + "\"");
		}
		if(!(this.antonym == null) && !this.antonym.equals("")){
			System.out.println("Antonyms: \"" + this.antonym + "\"");
		}
		System.out.println("*************************************************");
	}
}