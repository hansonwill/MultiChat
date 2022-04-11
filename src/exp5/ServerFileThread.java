package exp5;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ServerFileThread extends Thread{
	ServerSocket server = null;
	Socket socket = null;
	static List<Socket> list = new ArrayList<Socket>();  // Storage client
	
	public void run() {
		try {
			server = new ServerSocket(8090);
			while(true) {
				socket = server.accept();
				list.add(socket);
				// Open file transfer thread
				FileReadAndWrite fileReadAndWrite = new FileReadAndWrite(socket);
				fileReadAndWrite.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class FileReadAndWrite extends Thread {
	private Socket nowSocket = null;
	private DataInputStream input = null;
	private DataOutputStream output = null;
	
	public FileReadAndWrite(Socket socket) {
		this.nowSocket = socket;
	}
	public void run() {
		try {
			input = new DataInputStream(nowSocket.getInputStream());
			while (true) {
				// Get file name and file length
				String textName = input.readUTF();
				long textLength = input.readLong();
				// Send file name and file length to all clients
				for(Socket socket: ServerFileThread.list) {
					output = new DataOutputStream(socket.getOutputStream());
					if(socket != nowSocket) {
						output.writeUTF(textName);
						output.flush();
						output.writeLong(textLength);
						output.flush();
					}
				}
				//Send file content
				int length = -1;
				long curLength = 0;
				byte[] buff = new byte[1024];
				while ((length = input.read(buff)) > 0) {
					curLength += length;
					for(Socket socket: ServerFileThread.list) {
						output = new DataOutputStream(socket.getOutputStream());
						if(socket != nowSocket) {
							output.write(buff, 0, length);
							output.flush();
						}
					}
					if(curLength == textLength) {
						break;
					}
				}
			}
		} catch (Exception e) {
			ServerFileThread.list.remove(nowSocket);  // The thread closes and the corresponding socket is removed
		}
	}
}