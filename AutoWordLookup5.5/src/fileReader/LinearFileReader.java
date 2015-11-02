package fileReader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class LinearFileReader {
	
	BufferedReader reader = null;
	
	/**
	 * The constructor takes the filepath of the file to be read.
	 * 
	 * @param filepath - absolute filepath
	 */
	public LinearFileReader(String filepath){
		if(filepath == null){
			System.err.println("LinearFileReader: No such file!");
			return;
		}
		filepath = filepath.trim();
		if(filepath.endsWith(".xls")){
			System.err.println("LinearFileReader: Supplied xls file instead!");
			return;
		}
		String[] types = {".txt",".doc"};
		boolean validtype = false;
		for(int i=0; i< types.length; i++){
			if(filepath.endsWith(types[i])){
				validtype = true;
				break;
			}
		}
		if(!validtype){
			System.err.println("LinearFileReader: Invalid file format!");
			return;
		}
		try {
			this.reader = new BufferedReader(new FileReader(filepath));
		} catch (FileNotFoundException e) {
			System.err.println("LinearFileReader: Invalid file!");
			this.reader = null;
		}
	}
	
	/**
	 * This method reads the file and returns a string arry of words.
	 * @return
	 */
	public String[] getWords(){
		String line = "";
		Vector v = new Vector();
		try {
			while((line = reader.readLine()) != null){
				v.add(line.trim());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String[] arr = new String[v.size()];
		for(int i=0; i< v.size(); i++){
			arr[i] = (String) v.get(i);
		}
		return arr;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

