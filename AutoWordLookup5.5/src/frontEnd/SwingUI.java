package frontEnd;

import java.awt.Choice;
import java.awt.Component;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import processing.Backend;
import processing.WordList;
import dictionaries.Dictionary;
import dictionaries.DictionaryManager;
import fileWriter.Writer;

public class SwingUI extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GridBagConstraints constraints = new GridBagConstraints();
	
	/*
	 * Slab 1:
	 */
	private JLabel inputlabel;
	
	private JRadioButton lookupradio;
	
	private JLabel lookupinstrs;
	
	private JTextField inputpathbox;
	
	private JButton inputbrowsebutton;
	
	private JLabel websiteinstrs;
	
	private Choice websitechoices;
	
	/*
	 * Slab 2:
	 */
	private JRadioButton regenradio;
	
	private JLabel regeninstrs;
	
	private JTextField xmlpathbox;
	
	private JButton xmlbrowsebutton;
	
	/*
	 * Slab 3:
	 */
	private JLabel outputlabel;
	
	private JLabel htmlinstrs;
	
	private Choice outputchoices;
	
	private JTextField htmlpathbox;
	
	private JButton htmlbrowsebutton;
	
	/*
	 * Slab 4:
	 */
	
	private JLabel xmlinstrs;
	
	private JCheckBox xmlenabled;
	
	private JTextField xmloutpathbox;
	
	private JButton xmloutbrowsebutton;
	
	/*
	 * Slab 5:
	 */
	private JLabel statusbar;
		
	private JButton exitbutton;
	
	private JButton startbutton;
	
	
	public SwingUI(){
		
		// Create and specify a layout manager
	    this.setLayout(new GridBagLayout());
	    constraints.insets = new Insets(5, 5, 10, 10);
	    constraints.fill = GridBagConstraints.NONE;
	    
	    makeButtons();
	    
	    renderLayout();
		
	}
	
private void makeButtons(){
	
	/*
	 * Slab 1
	 */
	
	inputlabel = new JLabel("Input:");
		
		lookupradio =  new JRadioButton();
		lookupradio.setMnemonic(KeyEvent.VK_L);
		lookupradio.setActionCommand("lookup");
		lookupradio.setSelected(true);
		lookupradio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	radio(evt);
            }
        });
		
		lookupinstrs = new JLabel("Online Thesaurus Lookup");
		
		inputpathbox = new JTextField("Input .txt file to read from",25);
		inputpathbox.setEnabled(false);
		
		inputbrowsebutton = new JButton("Browse");
		inputbrowsebutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	fileBrowser("txt",inputpathbox,true);
            	checkInputs();
            }
        });
		
		
		websiteinstrs = new JLabel("Online Thesaurus:");
		
		websitechoices = new Choice();
		websitechoices.setSize(20, 5);
		String[] dicts = DictionaryManager.getAllDictionaryTitles();
		for(int i=0; i<dicts.length; i++){
			websitechoices.add(dicts[i]);
		}
				
		/*
		 * Slab 2:
		 */
		regenradio =  new JRadioButton();
		regenradio.setMnemonic(KeyEvent.VK_R);
		regenradio.setActionCommand("regenerate");
		regenradio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	radio(evt);
            }
        });
		
		ButtonGroup group = new ButtonGroup();
        group.add(lookupradio);
        group.add(regenradio);
		
		regeninstrs = new JLabel("Rebuild from Database:");
		
		xmlpathbox = new JTextField("Input xml file to read from", 25);
		xmlpathbox.setEnabled(false);
		
		xmlbrowsebutton = new JButton("Browse");
		xmlbrowsebutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	fileBrowser("xml",xmlpathbox,true);
            	checkInputs();
            }
        });
		xmlbrowsebutton.setEnabled(false);
		
		/*
		 * Slab 3:
		 */
		
		outputlabel = new JLabel("Output:");
		
		htmlinstrs = new JLabel("Primary Output Destination:");
		
		outputchoices = new Choice();
		outputchoices.add("Serial HTML");
		outputchoices.add("Tabular HMTL");
		//outputchoices.add("Database Entry Only");
		
		htmlpathbox = new JTextField("Output html file to save to",25);
		htmlpathbox.setEnabled(false);
		
		htmlbrowsebutton = new JButton("Save To");
		htmlbrowsebutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	fileBrowser("html",htmlpathbox,false);
            	checkInputs();
            }
        });
		
		/*
		 * Slab 4:
		 */
		
		xmlinstrs = new JLabel("Generate Database Entry (XML) - (Optional)");
		
		xmlenabled = new JCheckBox();
		xmlenabled.setSelected(true);
		
		xmloutbrowsebutton = new JButton("Save To");
		
		xmlenabled.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	if(!xmlenabled.isSelected()){
            		xmloutbrowsebutton.setEnabled(false);
            	}else{
            		xmloutbrowsebutton.setEnabled(true);
            	}
            }
        });
		
		
		xmloutpathbox = new JTextField("Output xml file to write to",25);
		xmloutpathbox.setEnabled(false);
		
		xmloutbrowsebutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	fileBrowser("xml",xmloutpathbox,false);
            	checkInputs();
            }
        });
		
		/*
		 * Slab 5:
		 */
		statusbar = new JLabel("Status: Awaiting valid inputs");
				
		exitbutton = new JButton("Exit");
		exitbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	System.exit(0);
            }
        });
		
		startbutton = new JButton("Start");
		startbutton.setEnabled(false);
		startbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				start();
			}
		});
		
	}
	
	private void renderLayout(){
				
		/*
		 * Slab 1:
		 */
		GBAssistant gba = new GBAssistant(this, constraints);
		
		gba.addComponent(inputlabel);
		gba.nextLine();	//Line 1:
		gba.addComponent(lookupradio);
		gba.addComponent(lookupinstrs);
		gba.nextLine(); // Line 2:
		gba.shiftRight(1);
		gba.addComponent(inputpathbox,3,1);
		gba.addComponent(inputbrowsebutton);
		gba.nextLine(); // Line 3:
		gba.shiftRight(1);
		gba.addComponent(websiteinstrs,2,1);
		gba.addComponent(websitechoices,2,1);
		
		/*
		 * Slab 2:
		 */
		gba.nextLine(); // Line 4:
		gba.nextLine(); // Line 5:
		gba.addComponent(regenradio);
		gba.addComponent(regeninstrs);
		gba.nextLine(); // Line 6:
		gba.shiftRight(1);
		gba.addComponent(xmlpathbox,3,1);
		gba.addComponent(xmlbrowsebutton);
		
		/*
		 * Slab 3:
		 */
		gba.nextLine(); // Line 7:
		gba.addComponent(outputlabel,4,3);
		gba.nextLine(); // Line 8:
		gba.nextLine(); // Line 9:
		gba.nextLine(); // Line 10:
		gba.shiftRight(1);
		gba.addComponent(htmlinstrs,2,1);
		gba.addComponent(outputchoices,2,1);
		gba.nextLine(); // Line 11:
		gba.shiftRight(1);
		gba.addComponent(htmlpathbox,3,1);
		gba.addComponent(htmlbrowsebutton);
		
		/*
		 * Slab 4:
		 */
		
		gba.nextLine(); // Line 12:
		gba.shiftRight(1);
		gba.addComponent(xmlinstrs,3,1);
		gba.addComponent(xmlenabled,2,1);
		gba.nextLine(); // Line 13:
		gba.shiftRight(1);
		gba.addComponent(xmloutpathbox,3,1);
		gba.addComponent(xmloutbrowsebutton);
		
		/*
		 * Slab 5:
		 */
		gba.nextLine();
		gba.shiftRight(1);
		gba.addComponent(statusbar);
		gba.nextLine(); // Line 16:
		gba.shiftRight(1);
		gba.addComponent(exitbutton,3,1);
		gba.addComponent(startbutton);
		
	}
	
	private void radio(ActionEvent event){
		if(event.getActionCommand().equals("lookup")){
			//disable whole of slab 2
			xmlpathbox.setText("Input .xml file to read from");
			xmlbrowsebutton.setEnabled(false);
			//enable whole of slab 1:
			inputbrowsebutton.setEnabled(true);
			websitechoices.setEnabled(true);
			xmlenabled.setEnabled(true);
			
		}else{
			//disable whole of slab 1
			inputbrowsebutton.setEnabled(false);
			inputpathbox.setText("Input .txt file to read from");
			websitechoices.setEnabled(false);
			//Disable writing to xml - a waste!
			xmlenabled.setSelected(false);
			xmlenabled.setEnabled(false);
			xmloutbrowsebutton.setEnabled(false);
			//Enable whole of slab 2:
			xmlbrowsebutton.setEnabled(true);
		}
	}
	
	private void fileBrowser(String extension, JTextField displaypath, boolean openingfile){
		Frame f = new Frame();
		FileDialog filebrowsers = new FileDialog(f,"Browse",FileDialog.LOAD);
		filebrowsers.setVisible(true);
		if (filebrowsers.getFile() == null) {
			return;
		}
		String selectedItem = filebrowsers.getDirectory() + File.separator +
		filebrowsers.getFile();
		
		File test = new File(selectedItem);
		if(test.exists()){
			if(!openingfile){
				JOptionPane.showMessageDialog(null, "Such a file already exists \n please provide a different file name", "Error", JOptionPane.ERROR_MESSAGE);
				/*JOptionPane optionPane = new JOptionPane(
					    "Such a file already exists\n"
					    + "Would you like to overwrite the file?",
					    JOptionPane.QUESTION_MESSAGE,
					    JOptionPane.YES_NO_OPTION);
				optionPane.setVisible(true)*/
				/*JOptionPane.showInternalConfirmDialog(
						this,
						"Such a file already exists\n"
					    + "Would you like to overwrite the file?", 
					    "Warning",
					    JOptionPane.YES_NO_OPTION,
					    JOptionPane.WARNING_MESSAGE,
					    null);*/
			return;
			}
		}else{
			if(openingfile){
				JOptionPane.showMessageDialog(null, "Such a file does not exist!\nPlease select a valid .txt file", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		
		int indx = filebrowsers.getFile().lastIndexOf(".");
		
		if(openingfile){
			//need to validate extension
			String ext = null;
			if(indx >= 0){
				ext = filebrowsers.getFile().substring(indx+1);
			}else{
				JOptionPane.showMessageDialog(null, "Please select a file with a valid extension", "Error", JOptionPane.ERROR_MESSAGE);
			}
		    if(!ext.equals(extension)){
		    	JOptionPane.showMessageDialog(null, "Please select a file with ." + extension + " extension", "Error", JOptionPane.ERROR_MESSAGE);
		    }else{
		    	displaypath.setText(selectedItem);
		    	return;
		    }
		}else{
			//saving file. Need to add extension to file if not added
			if(indx >= 0){
				if(!filebrowsers.getFile().endsWith("."+extension)){
					//need to remove the wrong extension and add the correct extension:
					selectedItem = filebrowsers.getDirectory() + File.separator + filebrowsers.getFile().substring(0,indx) + "." + extension;
				}
			}else{
				//no extension. need to add extension
				selectedItem = selectedItem + "." + extension;
			}
			displaypath.setText(selectedItem);
			return;
		}
		
	}
	
	private void checkInputs(){
		if(validateAll()){
			startbutton.setEnabled(true);
		}else{
			startbutton.setEnabled(false);
		}
	}
	
	private boolean validateAll(){
		//first ensure valid input:
		//1. Online lookup:
		if(lookupradio.isSelected()){
			if(!inputpathbox.getText().toLowerCase().endsWith(".txt")){
				return false;
			}
		}else{
			if(!xmlpathbox.getText().toLowerCase().endsWith(".xml")){
				return false;
			}
		}
		//Step 2: Validate Output(s)
		if(xmlenabled.isSelected()){
			if(!xmloutpathbox.getText().toLowerCase().endsWith(".xml")){
				return false;
			}				
		}
		if(!htmlpathbox.getText().toLowerCase().endsWith(".html")){
			return false;
		}
		return true;
	}
	
	private void start(){
		
		startbutton.setText("Processing");
				
		WordList list = null;
		
		if(lookupradio.isSelected()){
			Dictionary[] dicttypes = DictionaryManager.getAllDictionaries();
			Dictionary dictionary = dicttypes[websitechoices.getSelectedIndex()];
			statusbar.setText("Looking up words online. Please wait......");
			String info = "While looking up the words\nthe application migh appear\nnonresponsive.\n";
			info += "Please be patient while\nit looks up the words\nyou requested.";
			JOptionPane.showMessageDialog(this, info,"Processing...",JOptionPane.INFORMATION_MESSAGE);
			list = Backend.lookupOnline(inputpathbox.getText(),dictionary);
		}else{
			statusbar.setText("Reading the XML File. Please Wait.....");
			list = Backend.regenFromXML(xmlpathbox.getText());
		}
		statusbar.setText("Writing to File(s)");
		//Now writing to file:
		
		startbutton.setText("Start");
		
		//Newly added feature - opening the written file!!!!!
		if(xmlenabled.isSelected()){
			Backend.writeToFile(list, xmloutpathbox.getText(), Writer.XML);
			BareBonesBrowserLaunch.openURL(xmloutpathbox.getText());
		}
		if(outputchoices.getSelectedIndex() == 0){
			Backend.writeToFile(list, htmlpathbox.getText(), Writer.SERIALHTML);
			BareBonesBrowserLaunch.openURL(htmlpathbox.getText());
			
		}else if(outputchoices.getSelectedIndex() == 1){
			Backend.writeToFile(list, htmlpathbox.getText(), Writer.TABULARHTML);
			BareBonesBrowserLaunch.openURL(htmlpathbox.getText());
			
		}else if(!xmlenabled.isSelected() && outputchoices.getSelectedIndex() == 1){
			Backend.writeToFile(list, xmloutpathbox.getText(), Writer.XML);
			BareBonesBrowserLaunch.openURL(xmloutpathbox.getText());
		}
		statusbar.setText("Completed! Please collect your requested files.");
	}
	
	public static void main(String[] args) {
	    JFrame f = new JFrame();
	    f.addWindowListener(new WindowAdapter() {
	      public void windowClosing(WindowEvent e) {
	        System.exit(0);
	      }
	    });

	    f.setContentPane(new SwingUI());
	    f.pack();
	    f.setVisible(true);
	}
	
	private class GBAssistant{
		
		private GridBagConstraints constraints;
		
		private JPanel panel;
		
		private int x=0, y=0;
		
		private int gw=1, gh=1;
		
		public GBAssistant(JPanel panel, GridBagConstraints constraints){
			this.panel = panel;
			this.constraints = constraints;
		}
		
		public void nextLine(){
			shiftDown(1);
		}
		
		public void shiftRight(int shift){
			x+=shift;
		}
		
		public void shiftDown(int shift){
			x=0;
			y+= shift;
			gw=1;
			gh=1;
		}
		
		public void addComponent(Component component, int width, int height) {
		    
			if(width < 1){
		    	width = 1;
		    }
		    if(height < 1){
		    	height = 1;
		    }
			
			constraints.gridx = x;
		    constraints.gridy = y;
		    gw = width;
		    gh = height;
		    constraints.anchor = GridBagConstraints.WEST;
		    constraints.weightx = 1;
		    constraints.gridwidth = gw;
		    constraints.gridheight = gh;
		    
		    //System.out.println("Adding: " + component.getClass().getSimpleName() + ", x="+x+",y="+y+", gw="+gw+", gh="+gh);
		    
		    panel.add(component, constraints);
		    
		    //Updating the properties:
		    x+=width;
		}
		
		public void addComponent(Component component){
		    addComponent(component, 1, 1);
		}
		
	}

}
