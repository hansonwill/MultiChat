package exp5;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server{
	static ServerSocket server = null;
	static Socket socket = null;
	static List<Socket> list = new ArrayList<Socket>();  //Storage client
	
	public static void main(String[] args) {
		MultiChat multiChat = new MultiChat();  // New chat system interface
		try {
			// The thread that opens file transfer to the client on the server side
			ServerFileThread serverFileThread = new ServerFileThread();
			serverFileThread.start();
			server = new ServerSocket(8081);  //Server side socket (can only be established once)

			//Wait for the connection and start the corresponding thread
			while (true) {
				socket = server.accept();
				list.add(socket);

				ServerReadAndPrint readAndPrint = new ServerReadAndPrint(socket, multiChat);
				readAndPrint.start();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}


class ServerReadAndPrint extends Thread{
	Socket nowSocket = null;
	MultiChat multiChat = null;
	BufferedReader in =null;
	PrintWriter out = null;

	public ServerReadAndPrint(Socket s, MultiChat multiChat) {
		this.multiChat = multiChat;
		this.nowSocket = s;
	}
	
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(nowSocket.getInputStream()));  // Input stream
			// Obtain client information and send the information to all clients
			while (true) {
				String str = in.readLine();
				// Send to all clients
				for(Socket socket: Server.list) {
					out = new PrintWriter(socket.getOutputStream());  // Create a new socket for each client
					if(socket == nowSocket) {  // Send to current client
						out.println("(You)" + str);
					}
					else {  // Send to other clients
						out.println(str);
					}
					out.flush();  //Empty cache

				}

				multiChat.setTextArea(str);
			}
		} catch (Exception e) {
			Server.list.remove(nowSocket);
		}
	}
}
