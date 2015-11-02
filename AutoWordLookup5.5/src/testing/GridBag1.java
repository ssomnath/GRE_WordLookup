package testing;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GridBag1 extends JPanel {
  GridBagConstraints constraints = new GridBagConstraints();

  public GridBag1() {
    setLayout(new GridBagLayout());
    int x, y; // for clarity
    addGB(new JButton("North"), x = 1, y = 0);
    addGB(new JButton("West"), x = 0, y = 1);
    addGB(new JButton("Center"), x = 1, y = 1);
    addGB(new JButton("East"), x = 2, y = 1);
    addGB(new JButton("South"), x = 1, y = 2);
  }

  private void addGB(Component component, int x, int y) {
    constraints.gridx = x;
    constraints.gridy = y;
    add(component, constraints);
  }

  public static void main(String[] args) {
    JFrame frame = new JFrame("GridBag1");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(225, 150);
    frame.setLocation(200, 200);
    frame.setContentPane(new GridBag1());
    frame.setVisible(true);
  }
}

   
    
  
