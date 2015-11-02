package dictionaries;

import java.util.Vector;

import definitions.DictionaryEntry;
import definitions.ThesaurusEntry;

public interface WebFunctionalities {
	
	public abstract Vector<ThesaurusEntry> getThesaurusEntries(String target);
	
	public abstract Vector<DictionaryEntry> getDictionaryEntries(String target);
	
	public abstract String getName();
	
	public abstract Dictionary getDictionaryType();
}

