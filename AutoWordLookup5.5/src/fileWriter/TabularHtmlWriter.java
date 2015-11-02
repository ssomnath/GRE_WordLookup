package fileWriter;

import java.util.Vector;

import processing.WordList;

import definitions.DictionaryEntry;
import definitions.ThesaurusEntry;


public class TabularHtmlWriter extends HtmlWriter{
	
	private static String style = null;
	
	private boolean zebra = true;

	public TabularHtmlWriter(String outputPath, WordList wl) {
		super(outputPath,wl);
		if(style == null){
			refreshStyle();
		}
	}
	
	private void refreshStyle(){
		
		style = "<style type=\"text/css\">\n";
		style += "body {\n";
		style += "\tfont-family: \"Trebuchet MS\", Arial, Helvetica, sans-serif;\n";
		style += "\tfont-size: 13px;\n";
		style += "\tmargin: 0px 30px 0px 30px;\n";
		style += "\tpadding: 0px;\n";
		style += "}\n\n";
		style += "h1 {\n";
		style += "\tcolor: #C11A00;\n";
		style += "\tfont-size: 38px;\n";
		style += "}\n\n";
		style += "#zebra {\n";
		style += "\tfont-size:13px;\n";
		style += "\tborder:0;\n";
		style += "\twidth:100%;\n";
		style += "\tpadding:5;\n";
		style += "\tborder-collapse:collapse;\n";
		style += "\tcolor:#3E3E3E\n";
		style += "}\n\n";
		style += "#zebra td, th {\n";
		style += "\tfont-size:1em;\n";
		style += "\t/*border:1px solid #BBBBFF;*/\n";
		style += "\tpadding:10px 7px 10px 7px;\n";
		style += "}\n\n";
		style += "#zebra th {\n";
		style += "\tfont-size:1.5em;\n";
		style += "\ttext-align:left;\n";
		style += "\tbackground-color:#000066;\n";
		style += "\tcolor:#ffffff;\n";
		style += "}\n\n";
		style += "#zebra tr.alt td {\n";
		style += "\tcolor:#00004A;\n";
		style += "\tbackground-color:#CCCCFF;\n";
		style += "}\n\n";
		style += "</style>\n";
	}
	
	String getStyleString(){
		return style;
	}
	
	/**
	 * This method is responsible for taking a vector of DictionaryEntry
	 * objects and turning them into a html snippet string.
	 * @param vect - Vector containing DictioanryEntry objects
	 * @return - string of html snippet format
	 */
	public String dictVectorToHtml(Vector<DictionaryEntry> vect){
		if(vect == null){
			return "";
		}
		if(vect.size() == 0){
			return "";
		}
		
		DictionaryEntry current = (DictionaryEntry) vect.get(0);
		String output = ""; 
		
		
		for(int i=0; i<vect.size(); i++){
			if(zebra){
				output = "\t\t<TR class=\"alt\">\n";
			}else{
				output = "\t\t<TR>\n";
			}
			
			output += "\t\t\t<TD>" + current.word + "</TD>\n";
			current = vect.get(i);
			boolean meaning = false;
			if(current.meanings != null){
				output += "\t\t\t<TD>";
				meaning = true;
				for(int j=0; j< current.meanings.size(); j++){
					if(j==0){
						output += "<B>"+(i+1)+":</B>";
					}
					output += "&nbsp;" + current.meanings.get(j) + ", ";
				}
			}
			output += "<BR>\n";
			if(current.sentence != null){
				if(!meaning){
					output += "\t\t\t<TD>";
					meaning = true;
				}
				for(int j=0; j< current.sentence.size(); j++){
					output += "\t&nbsp;&lt;" + italicizeTarget(current.word, (String) current.sentence.get(j)) + "&gt; ";
				}
				
			}
			if(meaning){
				meaning = false;
				//adding ending cell tag
				output += "\t\t\t</TD>\n";
			}
			
			output += "\t\t\t<TD>";
			if(!current.synonym.equals("")){
				output += current.synonym;
			}else{
				output += "&nbsp;";
			}
			output += "</TD>\n";
			
			//last column never has any data:
			output += "\t\t\t<TD>&nbsp;</TD>";
			
			output += "\t\t</TR>";
		}
		return output;	
	}
	
	/**
	 * This method is responsible for taking a vector of thesaurusEntry
	 * objects and turning them into a html snippet string.
	 * @param vect - Vector containing ThesaurusEntry objects
	 * @return - string of html snippet format
	 */
	public String thesVectorToHtml(Vector<ThesaurusEntry> vect){
		if(vect == null){
			return "";
		}
		if(vect.size() == 0){
			return "";
		}
		
		ThesaurusEntry current = vect.get(0);
		String output = "";
		
		for(int i=0; i<vect.size(); i++){
			current = (ThesaurusEntry) vect.get(i);
			if(zebra){
				output = "\t\t<TR class=\"alt\">\n";
			}else{
				output = "\t\t<TR>\n";
			}
			
			output += "\t\t\t<TD>" + current.word + "</TD>\n";
			
			output += "\t\t\t<TD>";
			if(current.meaning != null && current.sentence != null){
				output += "<B>"+(i+1)+":</B>&nbsp;" + current.meaning + "&nbsp; &lt;"+ italicizeTarget(current.word, current.sentence) +".&gt;";
			}else if(current.meaning != null && current.sentence == null){
				output += "<B>"+(i+1)+":</B>&nbsp;" + current.meaning;	
			}
			output += "\n\t\t\t</TD>\n";
			
			output += "\t\t\t<TD>";
			if(current.synonym != null){
				output += current.synonym + "<BR>\n";
			}
			if(current.relword != null){
				output += ", " + current.relword + "<BR>\n";
			}
			output += "\t\t\t&nbsp;</TD>\n";
			
			
			output += "\t\t\t<TD>";
			if(current.antonym != null){
				output += current.antonym + "<BR>\n";
			}
			if(current.nearant != null){
				output += ", " + current.nearant + "<BR>\n";
			}
			output += "\t\t\t</TD>\n";
			
			output += "\t\t&nbsp;</TR>\n";
		}
		return output;
	}

	@Override
	public String getContent(Vector[] entries) {
		
		String output = "";
		for(int i=0; i<entries.length; i++){
			zebra = !zebra;
			output += getHtml(entries[i]);
		}
		
		String prev = "<TABLE id=\"zebra\">\n";
		prev += "\t\t<TR>\n\t\t\t<TH width=\"10%\">Word</TH>\n\t\t\t<TH width=\"60%\">Meaning & Sentence</TH>\n\t\t\t<TH>Synonyms</TH>\n\t\t\t<TH>Antonyms</TH>\n\t\t</TR>\n";
		String end = "\n\t</TABLE>\n";
		
		return (prev + output + end);
	}

}
