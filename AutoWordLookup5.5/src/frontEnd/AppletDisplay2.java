package frontEnd;


import java.applet.Applet;
import java.awt.Button;
import java.awt.Choice;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import dictionaries.Dictionary;
import fileWriter.Writer;

import processing.Backend;




/**
 * This class is used to rendering an applet front end
 * for the AWL Application
 * 
 * @author Suhas Somnath
 *
 */
public class AppletDisplay2 extends Applet implements ActionListener{

	private static final long serialVersionUID = 1L;
	Label firstinstructions = null;
	Button browse = null;
	FileDialog filebrowsers = null;
	Label filechoice = null;
	Label websiteinstrs = null;
	Choice websitechoices = null;
	Label outputinstrs = null;
	Choice outputchoices = null;
	Label numthreadsinstrs = null;
	Choice numthreadschoices = null;
	Button exitbutton = null;
	Button overridefilepath = null;
	Button startbutton = null;
	
	/**
	 * Rendering the display:
	 */
	public void init() 
	{
		// Now we will use the FlowLayout
		setLayout(null);
		firstinstructions = new Label("Select the file to read:");
		add(firstinstructions);
		browse = new Button("Browse");
		browse.addActionListener(this);
		add(browse);
		filechoice = new Label("");
		add(filechoice);
		websiteinstrs = new Label("Website:");
		add(websiteinstrs);
		websitechoices = new Choice();
		websitechoices.add("Merriam-Webster");
		websitechoices.add("Word Web Online");
		//websitechoices.addComponentListener((ComponentListener) this);
		add(websitechoices);
		outputinstrs = new Label("Output method:");
		add(outputinstrs);
		outputchoices = new Choice();
		outputchoices.add("Serial HTML");
		outputchoices.add("Tabular HTML");
		//outputchoices.addComponentListener((ComponentListener) this);
		add(outputchoices);
		numthreadsinstrs = new Label("(Optional) Number of threads:");
		add(numthreadsinstrs);
		numthreadschoices = new Choice();
		for (int i = 1; i < 11; i++){
			if(i == 1){
				numthreadschoices.add(i+" (slow)");
			}else if(i == 5){
				numthreadschoices.add(i+" (medium)");
			}else if(i == 10){
				numthreadschoices.add(i+" (fast)");
			}else{
				numthreadschoices.add(i+"");
			}
		}
		//numthreadschoices.addComponentListener((ComponentListener) this);
		add(numthreadschoices);
		exitbutton = new Button("Exit");
		exitbutton.addActionListener(this);
		add(exitbutton);
		overridefilepath = new Button("Save as");
		overridefilepath.addActionListener(this);
		overridefilepath.setEnabled(false);
		add(overridefilepath);
		startbutton = new Button("Start");
		startbutton.addActionListener(this);
		startbutton.setEnabled(false);
		add(startbutton);
		/*
		 * Setting the coordinates:
		 */
		firstinstructions.setBounds(20,20,200,30);
		filechoice.setBounds(20,60,250,30);
		browse.setBounds(280,60,50,25);
		
		websiteinstrs.setBounds(20,140,150,30);
		websitechoices.setBounds(200,140,125,30);
		outputinstrs.setBounds(20,180,150,30);
		outputchoices.setBounds(200,180,120,30);
		numthreadsinstrs.setBounds(20,220,175,30);
		numthreadschoices.setBounds(200,220,100,30);
		exitbutton.setBounds(20,260,50,25);
		overridefilepath.setBounds(90,260,75,25);
		startbutton.setBounds(185,260,50,25);
	}


	// When the button is clicked this method will get automatically called
	// This is where you specify all actions.
	
	public void actionPerformed(ActionEvent evt) 
	{
		// Here we will ask what component called this method
		if (evt.getSource() == browse){	
			Frame f = new Frame();
			filebrowsers = new FileDialog(f,"Browse");
			filebrowsers.setVisible(true);
		    String selectedItem = filebrowsers.getFile();
		    if(selectedItem != null){
		    	getAppletContext().showStatus("Verifying file");
		    	int indx = selectedItem.lastIndexOf(".");
			    String ext = selectedItem.substring(indx+1);
			    if((!ext.equals("txt")) && (!ext.equals("doc"))){
			    	filechoice.setText("Please select a file with .txt or .doc extensions");
			    	getAppletContext().showStatus("Waiting for input file");
			    }else{
			    	getAppletContext().showStatus("File approved!, proceed with other options");
			    	selectedItem = filebrowsers.getDirectory() + selectedItem;
			    	filechoice.setText(selectedItem);
			    	//Assume linear file source
			    	getAppletContext().showStatus("All Clear! Hit Start!");
			    	//sheetchoices.setVisible(false);
			    	startbutton.setEnabled(true);
			    }
		    }
		   f.dispose();
		}else if(evt.getSource() == exitbutton){
			System.exit(0);
		}else if(evt.getSource() == startbutton){
			System.out.println("file to be used: " + filechoice.getText());
			System.out.println("website index: " + websitechoices.getSelectedIndex());
			System.out.println("Output type: " + outputchoices.getSelectedIndex());
			System.out.println("Number of threads: " + numthreadschoices.getSelectedIndex()+1);
			
			Backend back = null;
			Dictionary dictionary = Dictionary.MERRIAM_WEBSTER;
			if(websitechoices.getSelectedIndex() == 1){
				dictionary = Dictionary.WORLD_WEB_ONLINE;
			}
			Writer outputmode = Writer.SERIALHTML;
			if(outputchoices.getSelectedIndex() == 1){
				outputmode = Writer.TABULARHTML;
			}
			Backend.writeToFile(Backend.lookupOnline(filechoice.getText(), dictionary), filechoice.getText().substring(0,filechoice.getText().lastIndexOf(File.separator)) + File.separator + "Default.html", outputmode);
			
		}else{
			getAppletContext().showStatus("Waiting for input file");
		}
		
		// Actions of the wrongButton
		
	}
}

