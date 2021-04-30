package musichub.server;

import java.io.*;
import java.net.*;

public class ServerInstance extends Thread {
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	private MusicHubServer theHubServer;
	
	public ServerInstance(Socket s, MusicHubServer theHubServer) {
		this.socket = s;
		this.theHubServer = theHubServer;
	}
	
	public void run () {
		try {
			this.input = new ObjectInputStream(socket.getInputStream());
			this.output = new ObjectOutputStream(socket.getOutputStream());
			
			String text = (String)input.readObject();  //read the object received through the stream and deserialize it
			System.out.println("server received a text:" + text);
			
			switch(text) {
			case "AUDIOELEMENTS":
				this.sendAudioElements();
				break;
			case "ALBUMS":
				this.sendAlbums();
				break;
			case "PLAYLISTS":
				this.sendPlaylists();
				break;
			}
			
			//Student student = new Student(1234, "john.doe");
			//this.output.writeObject(student);		//serialize and write the Student object to the stream
			//this.output.writeChars("helo");
				
		} catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();

		} catch (ClassNotFoundException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
			try {
				output.close();
				input.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
	
	private void sendAudioElements() {
		try {
			this.output.writeObject(theHubServer.getAudioElements());
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	private void sendAlbums() {
		try {
			this.output.writeObject(theHubServer.getAlbums());
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	private void sendPlaylists() {
		try {
			this.output.writeObject(theHubServer.getPlaylists());
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
