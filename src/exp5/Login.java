package exp5;

import java.awt.*;
import javax.swing.*;

public class Login {
	JTextField textField = null;
	JPasswordField pwdField = null;
	ClientReadAndPrint.LoginListen listener=null;
	
	// function
	public Login() {
		init();
	}
	
	void init() {
		JFrame jf = new JFrame("Login");
		jf.setBounds(500, 250, 340, 230);
		jf.setResizable(false);  // Set whether to zoom

		JPanel jp1 = new JPanel();
		JLabel headJLabel = new JLabel("Login interface");
		headJLabel.setFont(new Font(null, 0, 35));  // Set the font type, style and size of the text
		jp1.add(headJLabel);
		
		JPanel jp2 = new JPanel();
		JLabel nameJLabel = new JLabel("Username：");
		textField = new JTextField(20);
		JLabel pwdJLabel = new JLabel("Password：    ");
		pwdField = new JPasswordField(20);
		JButton loginButton = new JButton("Login");
		JButton registerButton = new JButton("Sign up");  // no function set
		jp2.add(nameJLabel);
		jp2.add(textField);
		jp2.add(pwdJLabel);
		jp2.add(pwdField);
		jp2.add(loginButton);
		jp2.add(registerButton);
		
		JPanel jp = new JPanel(new BorderLayout());
		jp.add(jp1, BorderLayout.NORTH);
		jp.add(jp2, BorderLayout.CENTER);
		
		// Set monitoring
		listener = new ClientReadAndPrint().new LoginListen();
		listener.setJTextField(textField);
		listener.setJPasswordField(pwdField);
		listener.setJFrame(jf);
		pwdField.addActionListener(listener);
		loginButton.addActionListener(listener);
		
		jf.add(jp);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
	}
}


