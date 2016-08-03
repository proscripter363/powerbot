package Colaborate;

import javax.swing.border.Border;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Gui {

	private String pass = "";

    public Gui() {

        JFrame frame = new JFrame();
        frame.setSize(500, 280);     

        Border etched = (Border) BorderFactory.createEtchedBorder();

        JLabel L1 = new JLabel("<html>Please put in your password to make sure the bot keeps going.<br>If you don't feel comfortable with this, please close this window.<br><br>"
        					 + "Requirments: <br><br>"
        					 + "-EOC On <br>"
        					 + "-Lodestone Teleport in Slot 1 <br>"
        					 + "-Telekinetic Grab in Slot 2<br>"
        					 + "-Falador Teleport in Slot 3 <br>"
        					 + "-Must maximize window <br>"
        					 + "-33 Mage </html>");
        JButton B1 = new JButton("Proceed");
        JTextField T1 = new JTextField("Enter Password Here"); 
        JPanel panel= new JPanel();
        
        panel.add(L1, BorderLayout.EAST); 
        frame.add(B1, BorderLayout.AFTER_LINE_ENDS);
        frame.add(T1, BorderLayout.SOUTH);
        panel.setBorder(etched);

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        B1.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
            	pass = T1.getText();
            	frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });
        
    }
    
    public String getPass()
    {
    	return pass;
    }

}