package musichub.business;

import java.util.*;
import java.io.*;
import java.net.*;

/** 
 * <b>ServerConnection Class</b> 
 * 
 *  Handle the connection between the client and the server, read and deserialize inputs.
 *  
 *  @author Elouan Toy
 *
 */ 

public class ServerConnection {
	private String ip;
	private int port;
	
	private ObjectOutputStream output = null;
	private ObjectInputStream input = null;
	private Socket socket = null;
	
	/** 
	 * <b>ServerConnection constructor</b> 
	 * 
	 *  Launch the connection with the specified parameters.
	 *  
	 *  @param 	String ip ip address of the remote server
	 *  @param	int port port of the remote server
	 *  
	 *  @throws ConnectionFailureException
	 *
	 */ 
	
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
			throw new ConnectionFailureException(uhe.toString());
		}
		catch  (IOException ioe) {
			throw new ConnectionFailureException(ioe.toString());
		}
	}
	
	/** 
	 * 
	 *  Request a list of audio elements to the server, deserialize the response, check if it is the requested element and return it.
	 *  
	 *  @return		the list of audio elements
	 *  @see		List<AudioElement> 
	 *  
	 *  @throws 	ConnectionLostException
	 *
	 */ 
	
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
	
	/** 
	 * 
	 *  Send a list of audio elements to the server.
	 *  
	 *  @param	List<AudioElement> elements list of audio elements
	 *  
	 *  @throws ConnectionLostException
	 *
	 */ 
	
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
	
	/** 
	 * 
	 *  Send a list of albums to the server.
	 *  
	 *  @param List<Album> elements list of albums
	 *  
	 *  @throws ConnectionLostException
	 *
	 */ 
	
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
	
	/** 
	 * 
	 *  Send a list of playlists to the server.
	 *  
	 *  @param	List<PlayList> elements list of playlists
	 *  
	 *  @throws ConnectionLostException
	 *
	 */ 
	
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
	
	/** 
	 * 
	 *  Request an audio file from the server, open a new file in the tmp/audio folder with its UUID as name, write it and close it.
	 *  
	 *  @param	AudioElement element concerned audio element
	 *  
	 *  @throws ConnectionLostException
	 *
	 */ 
	
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
	
	/** 
	 * 
	 *  Request a list of albums to the server, deserialize the response, check if it is the requested element and return it.
	 *  
	 *  @return	The list of albums
	 *  @see 	List<Album>
	 *  
	 *  @throws ConnectionLostException
	 *
	 */ 
	
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
	
	/** 
	 * 
	 *  Request a list of playlists to the server, deserialize the response, check if it is the requested element and return it.
	 *  
	 *  @return the list of playlists
	 *  @see 	List<PlayList>
	 *  
	 *  @throws ConnectionLostException
	 *
	 */ 
	
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
	
	/** 
	 * 
	 *  Checks if a connection has been set up or not.
	 *  
	 *  @return boolean
	 *
	 */ 
	
	public boolean isSetup() {
		if(this.socket == null)
			return false;
		else
			return true;
	}
	
	/** 
	 * 
	 *  Checks if the client is correctly connected.
	 *  
	 *  @return boolean
	 *
	 */ 
	
	public boolean isConnected() {
		return this.socket.isConnected();
	}
	
	/** 
	 * 
	 *  Send a ping to the server, this one should reply "pong"
	 *  
	 *  @return boolean
	 *
	 */ 
	
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
	
	/** 
	 * 
	 *  Initiate the connection to the server.
	 *
	 */ 
	
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
	
	/** 
	 * 
	 *  Close the initated connection.
	 *
	 */ 
	
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