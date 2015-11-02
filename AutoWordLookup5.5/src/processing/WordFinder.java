package processing;


import java.util.Vector;

import dictionaries.MerriamWebster;
import dictionaries.WebDictionary;
import dictionaries.WordWeb;




/**
 * This class is capable of reading an array of words
 * and get the meanings of the words from a
 * Merriam-Webster online dictionary/ thesaurus.
 * Also, this class does allow multi threading.
 * 
 * @author Suhas
 *
 */
public class WordFinder implements Runnable{
	
	private String[] words = null;
	
	private Vector[] result = null;
	
	private int numthreads = 4;
	
	private WebDictionary webdictionary = null;
	
	private int[][] threadlimits = null;
	
	private int status = numthreads;
	
	private long starttime = System.currentTimeMillis();
	
	private int completed = 0;
	
	/**
	 * The constructor takes a string array of words for which
	 * the meanings are to be found.
	 * @param webdictionary
	 * @param words
	 */
	public WordFinder(WebDictionary webdictionary, String[] words) {
		if(words == null){
			System.err.println("WordFinder: No words supplied!");
			return;
		}
		if(webdictionary == null){
			/*
			 * setting merriam webster as the default:
			 */
			webdictionary = new MerriamWebster();
		}
		this.words = words;
		this.webdictionary = webdictionary;
	}
		
	/**
	 * Returns the result of looking up meanings of words
	 * only if the entire lookup is completed.
	 * @return - Array of Thesaurus / Dictionary entries
	 */
	public Vector[] getResult(){
		if(this.status == 0){
			return result;
		}else{
			return null;
		}
	}
	
	/**
	 * This method is used to override the maximum number of threads
	 * that are to be used to lookup teh information
	 * @param numthreads
	 */
	public void setNumThreads(int numthreads){
		this.numthreads = numthreads;
		this.status = numthreads;
	}
	
	/**
	 * This is the main method that despatches multiple threads
	 * to fetch information online and retrieve the same.
	 *
	 */
	public void go(){
		
		int last = 0;
		/*
		 * Resetting the number of threads to one less than a quarter of the
		 * number of words if the number of threads are more than a quarter.
		 */
		if(numthreads < 1){
			numthreads = 4;
			System.out.println("Number of threads reset to " + this.numthreads);
		}
		if(this.words.length <= 11 && numthreads > 3){
			numthreads = 3;
		}
		
		this.status = numthreads;
			
		/*
		 * Setting the number of words each thread is responsible for
		 */
		int each = this.words.length / numthreads;	
		
		Thread[] threads = new Thread[numthreads];
		
		threadlimits = new int[numthreads][2];
		
		starttime = System.currentTimeMillis();
		
		result = new Vector[this.words.length];
		
		for(int i=0; i<numthreads; i++){
			threads[i] = new Thread(this);
			threads[i].setName(i+"");
			if(i == numthreads -1){
				threadlimits[i][0] = last;
				threadlimits[i][1] = this.words.length-1;
			}else{
				threadlimits[i][0] = last;
				threadlimits[i][1] = last + each;
			}
			last += each+1;
			threads[i].start();
		}
	}
	
	/**
	 * Once each thread is done looking up its share of words, it
	 * calls this method. This method is responsible for counting down
	 * the number of threads yet to complete their tasks.
	 * 
	 * @param threadnum - thread id for refference
	 */
	private void reportCompleted(int threadnum){
		status--;
		if(status == 0){
			System.out.println("All threads completed!");
			System.out.println("Perfomance Report:");
			starttime = ((System.currentTimeMillis() - starttime)/1000);
			try{
				long rate = this.words.length/starttime;
				System.out.println(this.numthreads + " Threads, Total Time taken = " + starttime + " seconds, " + rate + " words/second.");
			}catch(ArithmeticException e){
				e.printStackTrace();
			}
		}
	}

	/**
	 * This is the method for running the thread. It reads the subsection
	 * of the words and stores the result of the web lookup in the 
	 * global Vector array called result.
	 */
	public void run(){
		/*
		 * Finding the name of the thread:
		 */
		int threadnum = -1;
		try {
			threadnum = Integer.parseInt(Thread.currentThread().getName());
		} catch (NumberFormatException n) {
			System.err.println("Something went wrong with thread name...");
			return;
		}
		if(threadnum >= numthreads || threadnum < 0){
			System.err.println("Something went wrong with thread name...");
			return;
		}
		/*
		 * Preliminary checks:
		 */
		if(webdictionary == null){
			webdictionary = new MerriamWebster();
		}
		
		/*
		 * Using the thread name to find the predetermined bounds
		 * for reading the file
		 */
		
		int start = this.threadlimits[threadnum][0];
		int end = this.threadlimits[threadnum][1];
			
		/*
		 * Gathering outputs:
		 */
		
		String last = "";
		for(int i = start; i <= end; i++){
			if(i >= words.length){
				break;
			}
			String current = words[i];
			if(current == null){
				addCompleted();
				continue;
			}
			current = current.trim();
			if(current.equals("")){
				addCompleted();
				continue;
			}
			/*
			 * Checking for duplicate words:
			 */
			if(current.equals(last)){
				addCompleted();
				continue;
			}
			last = current;
			
			double progress = (end - start);
			progress = (i - start) / progress;
			progress = 100 * progress;
			int intprog = (int) progress;
			
			System.out.println("Thread # = " + threadnum + ", current word = " + current + "(" + intprog + "%)");
			
			if(current == null){
				addCompleted();
				continue;
			}
			
			/*
			 * contacting online to get the result
			 */
			current = current.trim();
			if(!current.equals("")){
				
				Vector temp = webdictionary.getThesaurusEntries(current);
				if(temp == null){
					temp = webdictionary.getDictionaryEntries(current);
				}
				/*
				 * Trying to add a little bit of intelligence:
				 * removes the "ing", "ed", and other suffixes
				 * to see if entries exist now:
				 */
				String[] suffixes = {"ed","ing"};
				
				for(int j=0; j<suffixes.length; j++){
					
					if(!current.endsWith(suffixes[j])){ continue;}
					
					int garb = current.lastIndexOf(suffixes[j]);
					if(garb > 0){
						current = current.substring(0,garb);
						
						//Try again:
						
						temp = webdictionary.getThesaurusEntries(current);
						if(temp == null){
							temp = webdictionary.getDictionaryEntries(current);
						}
					}
				}
				
				
				if(temp == null){
					addCompleted();
					continue;
				}else{
					result[i] = temp;
					addCompleted();
				}
			}
		}
		//this.result = output;
		System.out.println("Thread #" + threadnum + " has finished downloading all information!");
		reportCompleted(threadnum);
	}
	
	private synchronized void addCompleted(){
		completed++;
	}
	
	public synchronized int getProgress(){
		return (int) ((completed / words.length)*100);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] test = {"abet", "abstract"};
		WordFinder wf = new WordFinder(new WordWeb(), test);
		wf.go();		
	}

}

