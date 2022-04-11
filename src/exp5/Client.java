package exp5;

import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class Client {
	// Main function, create new login window
	public static void main(String[] args) {
		new Login();
	}
}

/**
 *  It is responsible for the reading and writing of the client, as well as the monitoring of login and transmission. The reason why the monitoring of login and transmission is put here is that it needs to share some data, such as mysocket and textarea
 */
class ClientReadAndPrint extends Thread{
	static Socket mySocket = null;  // Be sure to add static, otherwise it will be cleared when creating a new thread
	static JTextField textInput;
	static JTextArea textShow;
	static JFrame chatViewJFrame;
	static BufferedReader in = null;
	static PrintWriter out = null;
	static String userName;
	
	// It is used to receive messages sent from the server
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));  // Input stream
			while (true) {
				String str = in.readLine();  //Get the information sent by the server

				textShow.append(str + '\n');  // Add to the text area of the chat client
				textShow.setCaretPosition(textShow.getDocument().getLength());  //Set the scroll bar at the bottom

			}
		} catch (Exception e) {}
	}
	
	/**********************Login listening (internal class)**********************/
	class LoginListen implements ActionListener{
		JTextField textField;
		JPasswordField pwdField;
		JFrame loginJFrame;  // The login window itself
		
		ChatView chatView = null;
		
		public void setJTextField(JTextField textField) {
			this.textField = textField;
		}
		public void setJPasswordField(JPasswordField pwdField) {
			this.pwdField = pwdField;
		}
		public void setJFrame(JFrame jFrame) {
			this.loginJFrame = jFrame;
		}
		public void actionPerformed(ActionEvent event) {
			userName = textField.getText();
			String userPwd = String.valueOf(pwdField.getPassword());  // getPassword method to get char array
			if(userName.length() >= 1 && userPwd.equals("123")) {  // The password is 123 and the username length is greater than or equal to 1
				chatView = new ChatView(userName);  // Create a new chat window, set the username of the chat window (static)
				// Establish a connection with the server
				try {
					InetAddress addr = InetAddress.getByName(null);
					mySocket = new Socket(addr,8081);  // client socket
					loginJFrame.setVisible(false);
					out = new PrintWriter(mySocket.getOutputStream());
					out.println("User【" + userName + "】enters the chat room! ");
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}

				ClientReadAndPrint readAndPrint = new ClientReadAndPrint();
				readAndPrint.start();

				ClientFileThread fileThread = new ClientFileThread(userName, chatViewJFrame, out);
				fileThread.start();
			}
			else {
				JOptionPane.showMessageDialog(loginJFrame, "The account number or password is wrong, please re-enter! ", "Warning", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	/**********************Chat interface monitoring (inner class) **********************/
	class ChatViewListen implements ActionListener{
		public void setJTextField(JTextField text) {
			textInput = text;
		}
		public void setJTextArea(JTextArea textArea) {
			textShow = textArea;
		}
		public void setChatViewJf(JFrame jFrame) {
			chatViewJFrame = jFrame;

			chatViewJFrame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					out.println("User【" + userName + "】leaves the chat room! ");
					out.flush();
					System.exit(0);
				}
			});
		}

		public void actionPerformed(ActionEvent event) {
			try {
				String str = textInput.getText();

				if("".equals(str)) {
					textInput.grabFocus();

					JOptionPane.showMessageDialog(chatViewJFrame, "Input is empty, please re-enter! ", "Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}
				out.println(userName + "Speaking：" + str);
				out.flush();
				
				textInput.setText("");
				textInput.grabFocus();
//				textInput.requestFocus(true);
			} catch (Exception e) {}
		}
	}
}


