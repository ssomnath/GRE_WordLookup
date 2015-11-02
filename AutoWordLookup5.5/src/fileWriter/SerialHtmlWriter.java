package fileWriter;

import java.util.Vector;

import processing.WordList;

import definitions.DictionaryEntry;
import definitions.ThesaurusEntry;


public class SerialHtmlWriter extends HtmlWriter{
	
	private static String style = null;

	public SerialHtmlWriter(String outputPath, WordList wl) {
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
		style += "\tcolor:#333\n";
		style += "}\n\n";
		style += "h1 {\n";
		style += "\tcolor: #C11A00;\n";
		style += "\tfont-size: 38px;\n";
		style += "}\n\n";
		style += "h3 {\n";
		style += "\tcolor: #C11A00;\n";
		style += "\tfont-size: 18px;\n";
		style += "\tfont-style:bold\n";
		style += "}\n\n";
		style += "hr {\n";
		style += "\tcolor:#000;\n";
		style += "\tbackground-color:#333;\n";
		style += "\theight: 1px;\n";
		style += "}\n\n";
		style += "table {\n";
		style += "\tfont-size:13px;\n";
		style += "\tborder:0;\n";
		style += "\twidth:100%;\n";
		style += "\tpadding:5;\n";
		style += "\tborder-spacing:5;\n";
		style += "}\n\n";
		style += "th {\n";
		style += "\twidth: 110px;\n";
		style += "\ttext-align:left\n";
		style += "}\n\n";
		style += ".synonym {\n";
		style += "\tcolor:#090;\n";
		style += "\tfont-style: normal;\n";
		style += "\tfont-weight: bold;\n";
		style += "}\n\n";
		style += ".related {\n";
		style += "\tcolor:#909;\n";
		style += "\tfont-style: normal;\n";
		style += "\tfont-weight: bold;\n";
		style += "}\n\n";
		style += ".near {\n";
		style += "\tcolor:#F60;\n";
		style += "\tfont-style: normal;\n";
		style += "\tfont-weight: bold;\n";
		style += "}\n\n";
		style += ".antonym {\n";
		style += "\tcolor:#F00;\n";
		style += "\tfont-style: normal;\n";
		style += "\tfont-weight: bold;\n";
		style += "}\n\n";
		style += "#content {\n";
		style += "\tmargin-left: 30px;\n";
		style += "\tmargin-bottom: 20px;\n";
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
	public String dictVectorToHtml(Vector vect){
		if(vect == null){
			return "";
		}
		if(vect.size() == 0){
			return "";
		}
		
		DictionaryEntry current = (DictionaryEntry) vect.get(0);
		String output = "<h3>" + current.word + ":</h3>\n";
		output += "\t<div id=\"content\">\n";
		
		for(int i=0; i<vect.size(); i++){
			current = (DictionaryEntry) vect.get(i);
			if(current.meanings != null){
				for(int j=0; j< current.meanings.size(); j++){
					if(j==0){
						output += "\t<B>"+(i+1)+":</B>";
					}
					output += "&nbsp;" + current.meanings.get(j) + ", ";
				}
			}
			output += "<BR>\n";
			if(current.sentence != null){
				for(int j=0; j< current.sentence.size(); j++){
					output += "\t&nbsp;&lt;" + italicizeTarget(current.word, (String) current.sentence.get(j)) + "&gt; ";
				}
			}
			output += "<BR>\n";
			if(!current.synonym.equals("")){
				output += "<table>\n\t<tr>\n";
				output += "\t\t<th><div class=\"synonym\">Synonyms:</div></th>\n";
				output += "\t\t<td>" + current.synonym + "</td>\n";
				output += "\t</tr>\n</table>\n";
			}
			if(i!=vect.size()-1){
				output += "<BR/>\n";
			}
		}
		output += "</div>\n";
		output += "<HR>\n";
		return output;
		
		
	}
	
	/**
	 * This method is responsible for taking a vector of thesaurusEntry
	 * objects and turning them into a html snippet string.
	 * @param vect - Vector containing ThesaurusEntry objects
	 * @return - string of html snippet format
	 */
	public String thesVectorToHtml(Vector vect){
		if(vect == null){
			return "";
		}
		if(vect.size() == 0){
			return "";
		}
		
		ThesaurusEntry current = (ThesaurusEntry) vect.get(0);
		String output = "<h3>" + current.word + "</h3>\n";	
		output += "\t<div id=\"content\">\n";
		
		for(int i=0; i<vect.size(); i++){
			current = (ThesaurusEntry) vect.get(i);
			if(current.meaning != null && current.sentence != null){
				output += "\t<B>"+(i+1)+":</B>&nbsp;" + current.meaning + "&nbsp; &lt;"+ italicizeTarget(current.word, current.sentence) +".&gt;<BR>\n";
			}
			boolean tableopen = false;
			if(current.synonym != null){
				tableopen = true;
				output += "<table>\n";
				output += "\t<tr>\n\t\t<th><div class=\"synonym\">Synonyms:</div></th>\n";
				output += "\t\t<td>" + current.synonym + "</td>\n\t</tr>\n";
			}
			if(current.relword != null){
				if(!tableopen){
					output += "<table>\n";
					tableopen = true;
				}
				output += "\t<tr>\n\t\t<th><div class=\"related\">Related Words:</div></th>\n";
				output += "\t\t<td>" + current.relword + "</td>\n\t</tr>\n";
			}
			if(current.nearant != null){
				if(!tableopen){
					output += "<table>\n";
					tableopen = true;
				}
				output += "\t<tr>\n\t\t<th><div class=\"near\">Near Antonyms:</div></th>\n";
				output += "\t\t<td>" + current.nearant + "</td>\n\t</tr>\n";
			}
			if(current.antonym != null){
				if(!tableopen){
					output += "<table>\n";
					tableopen = true;
				}
				output += "\t<tr>\n\t\t<th><div class=\"antonym\">Antonyms:</div></th>\n";
				output += "\t\t<td>" + current.antonym + "</td>\n\t</tr>\n";
			}
			if(tableopen){
				output += "</table>\n";
			}
			if(i!=vect.size()-1){
				output += "<BR/>\n";
			}	
		}
		output += "\t</div>\n";
		output += "<HR>\n";
		return output;
	}
	
	@Override
	public String getContent(Vector[] entries) {
		
		String output = "";
		for(int i=0; i<entries.length; i++){
			output += getHtml(entries[i]);
		}
		
		return output;
	}
}
