package musichub.server;

import java.io.*;

import java.net.*;
import java.util.*;

import musichub.business.*;

/** 
 * <b>ServerInstance Class</b> 
 * 
 *  Handle the connection with a client.
 *  
 *  @author Elouan Toy
 *
 */ 

public class ServerInstance extends Thread {
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	private MusicHubServer theHubServer;
	
	/** 
	 * <b>ServerInstance constructor</b> 
	 * 
	 *  Prepare the thread.
	 *  
	 *  @param
	 *  	Socket s: the socket created by the connection
	 *  	MusicHubServer theHubServer: pointer to the HubServer
	 *
	 */ 
	
	public ServerInstance(Socket s, MusicHubServer theHubServer) {
		this.socket = s;
		this.theHubServer = theHubServer;
	}
	
	/** 
	 * <b>ServerInstance run</b> 
	 * 
	 * Executed when the client is connected. Deserialize input data and read it to determine if it is a command or not.
	 *
	 */ 
	
	public void run () {
		try {
			this.input = new ObjectInputStream(socket.getInputStream());
			this.output = new ObjectOutputStream(socket.getOutputStream());
			
			Object inputReader = input.readObject();
			
			if(inputReader.getClass().getName() == "java.lang.String") {
				System.out.println(socket.getRemoteSocketAddress().toString() + ": " + inputReader);
				String inputReaderCommand = (String)inputReader;
				
				switch(inputReaderCommand) {
				case "GETAUDIOELEMENTS":
					this.sendAudioElements();
					break;
				case "GETALBUMS":
					this.sendAlbums();
					break;
				case "GETPLAYLISTS":
					this.sendPlaylists();
					break;
				case "ping":
					this.output.writeObject("pong");
					this.output.flush();
					break;
				}
			}
			else { //Il faut determiner quel type d'objet le serveur a reçu
				if(inputReader instanceof List<?>) { //On controle le contenu de la liste
					switch(this.checkListContent(inputReader)) {
					case 1:
						System.out.println(socket.getRemoteSocketAddress().toString() + ": UPDATE ELEMENTS");
						saveElements(inputReader);
						break;
					case 2:
						System.out.println(socket.getRemoteSocketAddress().toString() + ": UPDATE ALBUMS");
						saveAlbums(inputReader);
						break;
					case 3:
						System.out.println(socket.getRemoteSocketAddress().toString() + ": UPDATE PLAYLISTS");
						savePlaylists(inputReader);
						break;
					default:
						System.out.println("Incorrect array form.");
					}
				} else if(inputReader instanceof AudioElement) { //Le client nous demande le fichier audio d'un element audio
					AudioElement element = (AudioElement)inputReader;
					System.out.println(socket.getRemoteSocketAddress().toString() + ": GET AUDIO " + element.getContent());
					sendAudioFile(element);
				}
			}
			
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
	
	/** 
	 * <b>ServerInstance checkListContent</b> 
	 * 
	 * Control the content of a received list and return the type of the list (1: AudioElement, 2: Album, 3: PlayList, 0: error)
	 * 
	 * @param
	 * 	Object inputReader: the list to analyze
	 * 
	 * @return int 
	 *
	 */ 
	
	private int checkListContent(Object inputReader) {
		List<Object> list = new ArrayList<Object>();
		list = (List<Object>)inputReader;
		int checkElements = 0; //0: nd, 1: elements, 2: albums, 3: playlists
		for(Object obj : list) {
			if(checkElements == 0) { //On determine par le premier element quelle liste on est censé avoir
				if(obj.getClass().getName() == "musichub.business.Song" || obj.getClass().getName() == "musichub.business.AudioBook")
					checkElements = 1;
				if(obj.getClass().getName() == "musichub.business.Album")
					checkElements = 2;
				if(obj.getClass().getName() == "musichub.business.PlayList")
					checkElements = 3;
			}
			else { //Si la liste n'a pas de sens, on break
				if (checkElements == 1 && (obj.getClass().getName() != "musichub.business.Song" && obj.getClass().getName() != "musichub.business.AudioBook")) {
					checkElements = 0;
					break;
				} else if (checkElements == 2 && obj.getClass().getName() != "musichub.business.Album") {
					checkElements = 0;
					break;
				}
				else if (checkElements == 3 && obj.getClass().getName() != "musichub.business.PlayList") {
					checkElements = 0;
					break;
				}
			}
		}
		
		return checkElements;
	}
	
	/** 
	 * <b>ServerInstance sendAudioFile</b> 
	 * 
	 * Send to the client an audio file stored on the server.
	 * 
	 * @param
	 * 		AudioElement element: the audio element associated
	 *
	 */ 
	
	private void sendAudioFile(AudioElement element) {
		try {
			InputStream inputFile = new FileInputStream("files/content/" + element.getContent());
			byte[] bytes = new byte[16*1024];
			
			int count;
			while((count = inputFile.read(bytes)) > 0) {
				this.output.write(bytes, 0, count);
			}
			inputFile.close();
			this.output.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	/** 
	 * <b>ServerInstance sendAudioElements</b> 
	 * 
	 * Send the list of AudioElements to the client.
	 *
	 */ 
	
	private void sendAudioElements() {
		try {
			
			List<AudioElement> elementsToSend = theHubServer.getAudioElements();
			this.output.writeObject(elementsToSend);
			this.output.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	/** 
	 * <b>ServerInstance sendAlbums</b> 
	 * 
	 * Send the albums list to the client.
	 *
	 */ 
	
	private void sendAlbums() {
		try {
			
			List<Album> elementsToSend = theHubServer.getAlbums();
			this.output.writeObject(elementsToSend);
			this.output.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	/** 
	 * <b>ServerInstance sendPlaylists</b> 
	 * 
	 * Send the playlists list to the client.
	 *
	 */ 
	
	private void sendPlaylists() {
		try {
			
			List<PlayList> elementsToSend = theHubServer.getPlaylists();
			this.output.writeObject(elementsToSend);
			this.output.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	/** 
	 * <b>ServerInstance saveElements</b> 
	 * 
	 * Save a new list of audio elements into the memory, via the MusicHubServer
	 * 
	 * @param
	 * 		Object inputList: the list of audio elements
	 *
	 */ 
	
	private void saveElements(Object inputList) {
		List<AudioElement> list = new ArrayList<AudioElement>();
		list = (List<AudioElement>)inputList;
		theHubServer.updateElements(list);
		theHubServer.saveElements();
	}
	
	/** 
	 * <b>ServerInstance saveAlbums</b> 
	 * 
	 * Save a new list of albums into the memory, via the MusicHubServer
	 * 
	 * @param
	 * 		Object inputList: the list of albums
	 *
	 */ 
	
	private void saveAlbums(Object inputList) {
		List<Album> list = new ArrayList<Album>();
		list = (List<Album>)inputList;
		theHubServer.updateAlbums(list);
		theHubServer.saveAlbums();
	}
	
	/** 
	 * <b>ServerInstance savePlaylists</b> 
	 * 
	 * Save a new list of playlists into the memory, via the MusicHubServer
	 * 
	 * @param
	 * 		Object inputList: the list of playlists
	 *
	 */ 
	
	private void savePlaylists(Object inputList) {
		List<PlayList> list = new ArrayList<PlayList>();
		list = (List<PlayList>)inputList;
		theHubServer.updatePlaylists(list);
		theHubServer.savePlayLists();
	}
}
