package exp5;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

public class MultiChat {
	JTextArea textArea;
	
	// Used to add information to a text area
	void setTextArea(String str) {
		textArea.append(str+'\n');
		textArea.setCaretPosition(textArea.getDocument().getLength());  // Set the scroll bar at the bottom
	}
	
	// Constructor
	public MultiChat() {
		init();
	}
	
	void init() {
		JFrame jf = new JFrame("Server side");
		jf.setBounds(500,100,450,500);  // Set window coordinates and size
		jf.setResizable(false);  // Set to non scalable
		
		JPanel jp = new JPanel();  // New container
		JLabel lable = new JLabel("Multiplayer chat system (server side)");
		textArea = new JTextArea(23, 38);  // Create a new text area and set the length and width
		textArea.setEditable(false);  // Set to non modifiable
		JScrollPane scroll = new JScrollPane(textArea);  // Set scroll panel (load textarea)
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);  // Show vertical bar
		jp.add(lable);
		jp.add(scroll);
		
		jf.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		jf.add(jp);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
	}
}
