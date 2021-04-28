package musichub.server;

import java.io.*;
import java.net.*;

public class ServerInstance extends Thread {
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	public ServerInstance(int port) {
		try {
			ServerSocket ss = new ServerSocket(port);
			
			while (true) {
				Socket socketClient = ss.accept();
		        String message = "";

		        System.out.println("Connexion avec : "+socketClient.getInetAddress());

		        // InputStream in = socketClient.getInputStream();
		        // OutputStream out = socketClient.getOutputStream();

		        BufferedReader in = new BufferedReader(
		          new InputStreamReader(socketClient.getInputStream()));
		        PrintStream out = new PrintStream(socketClient.getOutputStream());
		        message = in.readLine();
		        out.println(message);

		        socketClient.close();
			}
				
		} catch (Exception e) {
				e.printStackTrace();
		}
	}
}
