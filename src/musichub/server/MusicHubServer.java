package musichub.server;

import java.io.*;
import java.net.*;

public class MusicHubServer {
	private ServerInstance serv;
	private String ip = "localhost";
	private ServerSocket ss;
	
	public MusicHubServer() {
		try {
			ss = new ServerSocket(6666);
			System.out.println("Server waiting for connection...");
			while (true) {
				Socket socket = ss.accept();
				System.out.println("Connected as " + ip);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
			if (ss != null && !ss.isClosed()) {
				System.out.println("Closing connection...");
				try {
					ss.close();
				} catch (IOException e) {
					e.printStackTrace(System.err);
				}
			}
		}
	}
}
