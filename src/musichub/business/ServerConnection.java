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
	
	public List<AudioElement> requestAudioElements(){
		List<AudioElement> list = new ArrayList<>();
		try {
			this.output.writeObject("GETAUDIOELEMENTS");
			while(this.input != null) { //tant qu'on reçoit qqch, on continue de lire l'input
				Object inputReceived = this.input.readObject();
				if(inputReceived instanceof AudioElement) {
					AudioElement newAudioElement = (AudioElement)inputReceived;
					list.add(newAudioElement);
				}
				else break;
			}
		} catch  (UnknownHostException uhe) {
			uhe.printStackTrace();
		}
		catch  (IOException ioe) {
			ioe.printStackTrace();
		}
		catch  (ClassNotFoundException cfe) {
			cfe.printStackTrace();
		}
		
		return list;
	}
	
	public List<Album> requestAlbums(){
		List<Album> list = new ArrayList<>();
		try {
			this.output.writeObject("GETALBUMS");
			while(this.input != null) { //tant qu'on reçoit qqch, on continue de lire l'input
				Object inputReceived = this.input.readObject();
				if(inputReceived instanceof Album) {
					Album newAlbum = (Album)inputReceived;
					list.add(newAlbum);
				}
				else break;
			}
		} catch  (UnknownHostException uhe) {
			uhe.printStackTrace();
		}
		catch  (IOException ioe) {
			ioe.printStackTrace();
		}
		catch  (ClassNotFoundException cfe) {
			cfe.printStackTrace();
		}
		
		return list;
	}
	
	public List<PlayList> requestPlaylists(){
		List<PlayList> list = new ArrayList<>();
		try {
			this.output.writeObject("GETPLAYLISTS");
			while(this.input != null) { //tant qu'on reçoit qqch, on continue de lire l'input
				Object inputReceived = this.input.readObject();
				if(inputReceived instanceof PlayList) {
					PlayList newPlaylist = (PlayList)inputReceived;
					list.add(newPlaylist);
				}
				else break;
			}
		} catch  (UnknownHostException uhe) {
			uhe.printStackTrace();
		}
		catch  (IOException ioe) {
			ioe.printStackTrace();
		}
		catch  (ClassNotFoundException cfe) {
			cfe.printStackTrace();
		}
		
		return list;
	}
	
	public boolean isSetup() {
		if(this.socket == null)
			return false;
		else
			return true;
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