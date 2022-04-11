package exp5;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


public class ChatView {
	String userName;  //Set when logging in by the client
	JTextField text;
	JTextArea textArea;
	ClientReadAndPrint.ChatViewListen listener;

	//Constructor
	public ChatView(String userName) {
		this.userName = userName ;
		init();
	}
	// Initialization function
	void init() {
		JFrame jf = new JFrame("Client");
		jf.setBounds(500,200,450,310);  //Set coordinates and size
		jf.setResizable(false);  // not zoom
		
		JPanel jp = new JPanel();
		JLabel lable = new JLabel("User：" + userName);
		textArea = new JTextArea("*****Login succeeded. Welcome to the multiplayer chat room！*****\n",12, 35);
		textArea.setEditable(true);  //Set to non modifiable

		JScrollPane scroll = new JScrollPane(textArea);  // Set scroll panel (load textarea)
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);  // Show vertical bar
		jp.add(lable);
		jp.add(scroll);
		
		text = new JTextField(20);
		JButton button = new JButton("Send");
		JButton openFileBtn = new JButton("Send files");
		jp.add(text);
		jp.add(button);
		jp.add(openFileBtn);

		// Set "open file" listening
		openFileBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showFileOpenDialog(jf);
			}
		});

		//Set "send" listening
		listener = new ClientReadAndPrint().new ChatViewListen();
		listener.setJTextField(text);  //Call the method of the policelisten class

		listener.setJTextArea(textArea);
		listener.setChatViewJf(jf);
		text.addActionListener(listener);  // Add listening to text box
		button.addActionListener(listener);  //Button to add listening


		jf.add(jp);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Set the function of the close icon in the upper right corner
		jf.setVisible(true);  // Set visible
	}

	void showFileOpenDialog(JFrame parent) {

		JFileChooser fileChooser = new JFileChooser();

		fileChooser.setCurrentDirectory(new File("C:/Users/Lenovo/Desktop"));

//        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("(txt)", "txt"));

        fileChooser.setFileFilter(new FileNameExtensionFilter("(txt)", "txt"));

		int result = fileChooser.showOpenDialog(parent);

		if(result == JFileChooser.APPROVE_OPTION) {

			File file = fileChooser.getSelectedFile();
			String path = file.getAbsolutePath();
			ClientFileThread.outFileToServer(path);
		}
	}
}

