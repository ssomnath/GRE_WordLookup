package testing;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import dictionaries.Dictionary;
import fileReader.InputType;
import fileWriter.Writer;

import processing.Backend;

public class MyProgressBar extends JFrame{
	
	JLabel label;
    JProgressBar pb;
    JButton button;
    Timer timer;
    Backend bk;
    
    private GridLayout layout = new GridLayout(3, 1, 2, 2);
    
    public MyProgressBar(){
    	
    	this.setSize(500, 350);
		
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		
		// create all buttons
		makeButtons();
		
		// lay out the buttons
		buttonLayout();
		
		setTitle("Auto Word Lookup (AWL 4.7) by Suhas Somnath");	
    }
    
    private void makeButtons(){
    	label = new JLabel("Hit Start to ");
    	
    	button = new JButton("Start!");
    	button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				button.setEnabled(false);
				String str = "<html>" + "<font color=\"#008000\">" + "<b>" + 
				"Downloading is in process......." + "</b>" + "</font>" + "</html>";
				label.setText(str);

				timer.start();
		    	Backend.writeToFile(Backend.lookupOnline("m_w_problems.txt", Dictionary.MERRIAM_WEBSTER), "backend_test.html", Writer.TABULARHTML);
			}
		});
    	
    	pb = new JProgressBar(0, 10);
        pb.setValue(0);
        pb.setStringPainted(true);
        
      //Create a timer.
        timer = new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	//int prog = bk.getProgress();
            	int prog = 95;
            	System.out.println("Progress so far = " + 11);
            	if (prog == 95){
            		Toolkit.getDefaultToolkit().beep();
            		timer.stop();
            		button.setEnabled(true);
            		pb.setValue(0);
            		String str = "<html>" + "<font color=\"#FF0000\">" + "<b>" + 
"Downloading completed." + "</b>" + "</font>" + "</html>";
          label.setText(str);
        }
                pb.setValue(prog);
            }
        });
        
    }
    
    private void buttonLayout(){
    	
    	getContentPane().setLayout(layout);
    	
    	add(button);
    	add(pb);
    	add(label);
    }
    
    public static void main(String[] args) {
    	java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MyProgressBar().setVisible(true);
            }
        });
    }

}
