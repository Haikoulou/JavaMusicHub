package musichub.business;

import java.io.*;
import java.net.*;

public class ServerConnection { //Non-functionnal
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Socket socket;
	
	public ServerConnection(String ip, int port) {
		try {
			System.out.println("Trying to reach server...");
			this.socket = new Socket(ip, port);
			System.out.println("Client connected");
			this.output = new ObjectOutputStream(socket.getOutputStream());
	        this.input = new ObjectInputStream(socket.getInputStream());
		} catch  (UnknownHostException uhe) {
			uhe.printStackTrace();
		}
		catch  (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public ObjectInputStream sendRequest(String request) {
		try {
			this.output.writeObject(request);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		return this.input;
	}
	
	public void CloseConnection() throws IOException {
		this.input.close();
		this.output.close();
		this.socket.close();
	}
}