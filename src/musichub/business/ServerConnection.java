package musichub.business;

import java.util.*;
import java.io.*;
import java.net.*;

public class ServerConnection {
	private String ip;
	private int port;
	
	private ObjectOutputStream output = null;
	private ObjectInputStream input = null;
	private Socket socket = null;
	
	public ServerConnection(String ip, int port) throws ConnectionFailureException{
		this.ip = ip;
		if(port < 1000 || port > 65535) throw new ConnectionFailureException("Incorrect port range.");
		this.port = port;
		
		System.out.println("Trying to reach server...");
		try {
			this.socket = new Socket(ip, port);
			System.out.println("Client connected!");
			this.output = new ObjectOutputStream(socket.getOutputStream());
	        this.input = new ObjectInputStream(socket.getInputStream());
	        
	        if(!this.isSetup() || !this.isConnected() || this.ping()) throw new ConnectionFailureException("The server cannot be reached. Please check your network configuration and try again.");
	        
	        this.CloseConnection();
		} catch  (UnknownHostException uhe) {
			uhe.printStackTrace();
		}
		catch  (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public List<AudioElement> requestAudioElements() throws ConnectionLostException{
		this.connect();
		if(!this.isConnected() || !this.isSetup()) throw new ConnectionLostException("The connexion to the server has been interrupted.");
		
		List<AudioElement> list = new ArrayList<>();
		try {
			this.output.writeObject("GETAUDIOELEMENTS");
			Object inputReceived = this.input.readObject();
			if(inputReceived instanceof List<?>) {
				list = (List<AudioElement>)inputReceived;
			}
			this.output.flush();
		} catch  (UnknownHostException uhe) {
			uhe.printStackTrace();
		}
		catch  (IOException ioe) {
			ioe.printStackTrace();
		}
		catch  (ClassNotFoundException cfe) {
			cfe.printStackTrace();
		}
		
		this.CloseConnection();
		return list;
	}
	
	public void sendElements(List<AudioElement> elements) throws ConnectionLostException{
		this.connect();
		if(!this.isConnected() || !this.isSetup()) throw new ConnectionLostException("The connexion to the server has been interrupted.");
		
		try {
			this.output.writeObject(elements);
			this.output.flush();
		} catch  (UnknownHostException uhe) {
			uhe.printStackTrace();
		}
		catch  (IOException ioe) {
			ioe.printStackTrace();
		}
		
		this.CloseConnection();
	}
	
	public void sendAlbums(List<Album> albums) throws ConnectionLostException{
		this.connect();
		if(!this.isConnected() || !this.isSetup()) throw new ConnectionLostException("The connexion to the server has been interrupted.");
		
		try {
			this.output.writeObject(albums);
			this.output.flush();
		} catch  (UnknownHostException uhe) {
			uhe.printStackTrace();
		}
		catch  (IOException ioe) {
			ioe.printStackTrace();
		}
		
		this.CloseConnection();
	}
	
	public void sendPlaylists(List<PlayList> playlists) throws ConnectionLostException{
		this.connect();
		if(!this.isConnected() || !this.isSetup()) throw new ConnectionLostException("The connexion to the server has been interrupted.");
		
		try {
			this.output.writeObject(playlists);
			this.output.flush();
		} catch  (UnknownHostException uhe) {
			uhe.printStackTrace();
		}
		catch  (IOException ioe) {
			ioe.printStackTrace();
		}
		
		this.CloseConnection();
	}
	
	public void getAudioFile(AudioElement element) throws ConnectionLostException{
		this.connect();
		if(!this.isConnected() || !this.isSetup()) throw new ConnectionLostException("The connexion to the server has been interrupted.");
		
		try {
				OutputStream outputFile = new FileOutputStream("tmp/audio/" + element.getUUID().toString() + "." + element.getFormat());
				this.output.writeObject(element);
				byte[] bytes = new byte[16*1024];
				int count;
		        while ((count = this.input.read(bytes)) > 0) {
		            outputFile.write(bytes, 0, count);
		        }
		        outputFile.close();
			this.output.flush();
		} catch  (UnknownHostException uhe) {
			uhe.printStackTrace();
		} catch  (IOException ioe) {
			ioe.printStackTrace();
		}
		
		this.CloseConnection();
	}
	
	public List<Album> requestAlbums() throws ConnectionLostException{
		this.connect();
		if(!this.isConnected() || !this.isSetup()) throw new ConnectionLostException("The connexion to the server has been interrupted.");
		
		List<Album> list = new ArrayList<>();
		try {
			this.output.writeObject("GETALBUMS");
			Object inputReceived = this.input.readObject();
			if(inputReceived instanceof List<?>) {
				list = (List<Album>)inputReceived;
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
		
		this.CloseConnection();
		return list;
	}
	
	public List<PlayList> requestPlaylists() throws ConnectionLostException{
		this.connect();
		if(!this.isConnected() || !this.isSetup()) throw new ConnectionLostException("The connexion to the server has been interrupted.");
		
		List<PlayList> list = new ArrayList<>();
		try {
			this.output.writeObject("GETPLAYLISTS");
			Object inputReceived = this.input.readObject();
			if(inputReceived instanceof List<?>) {
				list = (List<PlayList>)inputReceived;
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
		
		this.CloseConnection();
		
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
	
	private boolean ping() {
		String answer = new String();
		try {
			this.output.writeObject("ping");
			answer = (String) this.input.readObject();
		} catch  (UnknownHostException uhe) {
			uhe.printStackTrace();
		}
		catch  (IOException ioe) {
			ioe.printStackTrace();
		}
		catch  (ClassNotFoundException cfe) {
			cfe.printStackTrace();
		}
		
		if(answer == "pong") //ping pong
			return true;
		else
			return false;
	}
	
	private void connect() {
		try {
			this.socket = new Socket(this.ip, this.port);
			this.output = new ObjectOutputStream(socket.getOutputStream());
	        this.input = new ObjectInputStream(socket.getInputStream());
		} catch  (UnknownHostException uhe) {
			uhe.printStackTrace();
		}
		catch  (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	private void CloseConnection() {
		try {
			this.input.close();
			this.output.close();
			this.socket.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}