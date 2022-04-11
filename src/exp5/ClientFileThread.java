package exp5;

import java.io.*;
import java.net.*;
import javax.swing.*;

public class ClientFileThread extends Thread{
	private Socket socket = null;
	private JFrame chatViewJFrame = null;
	static String userName = null;
	static PrintWriter out = null;
	static DataInputStream fileIn = null;
	static DataOutputStream fileOut = null;
	static DataInputStream fileReader = null;
	static DataOutputStream fileWriter = null;
	
	public ClientFileThread(String userName, JFrame chatViewJFrame, PrintWriter out) {
		ClientFileThread.userName = userName;
		this.chatViewJFrame = chatViewJFrame;
		ClientFileThread.out = out;
	}
	

	public void run() {
		try {
			InetAddress addr = InetAddress.getByName(null);  // get host address
			socket = new Socket(addr, 8090);  // client socket
			fileIn = new DataInputStream(socket.getInputStream());  // input stream
			fileOut = new DataOutputStream(socket.getOutputStream());  // output stream
			// Receive files
			while(true) {
				String textName = fileIn.readUTF();
				long totleLength = fileIn.readLong();
				int result = JOptionPane.showConfirmDialog(chatViewJFrame, "Accept？", "Warning",
														   JOptionPane.YES_NO_OPTION);
				int length = -1;
				byte[] buff = new byte[1024];
				long curLength = 0;
				// Prompt box selection result, 0 is OK, 1 is canceled
				if(result == 0){
//					out.println("【" + userName + "receive files! 】");
//					out.flush();
					File userFile = new File("C:\\Users\\Lenovo\\Desktop\\接受文件" + userName);
					if(!userFile.exists()) {  // Create a new folder for the current user
						userFile.mkdir();
					}
					File file = new File("C:\\Users\\Lenovo\\Desktop\\接受文件" + userName + "\\"+ textName);
					fileWriter = new DataOutputStream(new FileOutputStream(file));
					while((length = fileIn.read(buff)) > 0) {  // write file to local
						fileWriter.write(buff, 0, length);
						fileWriter.flush();
						curLength += length;
//						out.println("【Receive progress :" + curLength/totleLength*100 + "%】");
//						out.flush();
						if(curLength == totleLength) {  // forced end
							break;
						}
					}
					out.println("【" + userName + "accept the files】");
					out.flush();

					JOptionPane.showMessageDialog(chatViewJFrame, "File storage address: \n" +
							"C:\\Users\\Lenovo\\Desktop\\接受文件" +
							userName + "\\" + textName, "Warning", JOptionPane.INFORMATION_MESSAGE);
				}
				else {  // Documents not accepted
					while((length = fileIn.read(buff)) > 0) {
						curLength += length;
						if(curLength == totleLength) {
							break;
						}
					}
				}
				fileWriter.close();
			}
		} catch (Exception e) {}
	}
	
	// Client sends file
	static void outFileToServer(String path) {
		try {
			File file = new File(path);
			fileReader = new DataInputStream(new FileInputStream(file));
			fileOut.writeUTF(file.getName());  // send file name
			fileOut.flush();
			fileOut.writeLong(file.length());  // Send file length
			fileOut.flush();
			int length = -1;
			byte[] buff = new byte[1024];
			while ((length = fileReader.read(buff)) > 0) {  // content
				
				fileOut.write(buff, 0, length);
				fileOut.flush();
			}
			
			out.println("【" + userName + "has successfully sent file! 】");
			out.flush();
		} catch (Exception e) {}
	}
}
