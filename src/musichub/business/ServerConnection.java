package musichub.business;

import java.io.*;
import java.net.*;

public class ServerConnection { //Non-functionnal
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Socket socket;
	
	public ServerConnection(String ip, int port) throws UnknownHostException, IOException, ClassNotFoundException {
		System.out.println("Trying to reach server...");
		this.socket = new Socket(ip, port);
		System.out.println("Client connected");
		this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(socket.getInputStream());
	}
	
	public void CloseConnection() throws IOException {
		this.input.close();
		this.output.close();
		this.socket.close();
	}
}