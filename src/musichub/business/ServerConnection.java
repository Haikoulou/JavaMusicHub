package musichub.business;

import java.util.*;
import java.io.*;
import java.net.*;

public class ServerConnection { //Non-functionnal
	private ObjectOutputStream output = null;
	private ObjectInputStream input = null;
	private Socket socket = null;
	
	public ServerConnection(String ip, int port) {
		System.out.println("Trying to reach server...");
		try {
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
	
	public List<Song> requestSongs(){
		List<Song> list = new ArrayList<>();
		try {
			this.output.writeChars("SONGS");
			list = (List<Song>) this.input.readObject();
		} catch  (UnknownHostException uhe) {
			uhe.printStackTrace();
		}
		catch  (IOException ioe) {
			ioe.printStackTrace();
		}
		catch  (ClassNotFoundException cfe) {
			cfe.printStackTrace();
		}
		
		this.input = null;
		this.output = null;
		
		return list;
	}
	
	public List<AudioBook> requestAudioBooks(){
		List<AudioBook> list = new ArrayList<>();
		try {
			this.output.writeChars("AUDIOBOOKS");
			list = (List<AudioBook>) this.input.readObject();
		} catch  (UnknownHostException uhe) {
			uhe.printStackTrace();
		}
		catch  (IOException ioe) {
			ioe.printStackTrace();
		}
		catch  (ClassNotFoundException cfe) {
			cfe.printStackTrace();
		}
		
		this.input = null;
		this.output = null;
		
		return list;
	}
	
	public List<Album> requestAlbums(){
		List<Album> list = new ArrayList<>();
		try {
			this.output.writeChars("ALBUMS");
			list = (List<Album>) this.input.readObject();
		} catch  (UnknownHostException uhe) {
			uhe.printStackTrace();
		}
		catch  (IOException ioe) {
			ioe.printStackTrace();
		}
		catch  (ClassNotFoundException cfe) {
			cfe.printStackTrace();
		}
		
		this.input = null;
		this.output = null;
		
		return list;
	}
	
	public List<PlayList> requestPlaylists(){
		List<PlayList> list = new ArrayList<>();
		try {
			this.output.writeChars("PLAYLISTS");
			list = (List<PlayList>) this.input.readObject();
		} catch  (UnknownHostException uhe) {
			uhe.printStackTrace();
		}
		catch  (IOException ioe) {
			ioe.printStackTrace();
		}
		catch  (ClassNotFoundException cfe) {
			cfe.printStackTrace();
		}
		
		this.input = null;
		this.output = null;
		
		return list;
	}
	
	public boolean isConnected() {
		return this.socket.isConnected();
	}
	
	public void CloseConnection() {
		try {
			this.input.close();
			this.output.close();
			this.socket.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}