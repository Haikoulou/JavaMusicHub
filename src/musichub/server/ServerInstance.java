package musichub.server;

import java.io.*;
import java.net.*;
import java.util.*;

import musichub.business.*;

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
				System.out.println("Server received an object: " + inputReader.getClass().toString() + " from " + socket.getRemoteSocketAddress().toString());
				if(inputReader instanceof List<?>) { //On controle le contenu de la liste
					switch(this.checkListContent(inputReader)) {
					case 1:
						saveElements(inputReader);
						break;
					case 2:
						saveAlbums(inputReader);
						break;
					case 3:
						savePlaylists(inputReader);
						break;
					default:
						System.out.println("Incorrect array form.");
					}
				} else if(inputReader instanceof AudioElement) { //Le client nous demande le fichier audio d'un element audio
					AudioElement element = (AudioElement)inputReader;
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
	
	private int checkListContent(Object inputReader) {
		List<Object> list = new ArrayList<Object>();
		list = (List<Object>)inputReader;
		int checkElements = 0; //0: nd, 1: elements, 2: albums, 3: playlists
		for(Object obj : list) {
			System.out.println("recu " + obj.getClass().getName());
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
	
	private void sendAudioFile(AudioElement element) {
		try {
			System.out.println("Sending " + element.getContent());
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
	
	private void sendAudioElements() {
		try {
			
			List<AudioElement> elementsToSend = theHubServer.getAudioElements();
			this.output.writeObject(elementsToSend);
			this.output.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	private void sendAlbums() {
		try {
			
			List<Album> elementsToSend = theHubServer.getAlbums();
			this.output.writeObject(elementsToSend);
			this.output.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	private void sendPlaylists() {
		try {
			
			List<PlayList> elementsToSend = theHubServer.getPlaylists();
			this.output.writeObject(elementsToSend);
			this.output.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	private void saveElements(Object inputList) {
		List<AudioElement> list = new ArrayList<AudioElement>();
		list = (List<AudioElement>)inputList;
		theHubServer.updateElements(list);
		theHubServer.saveElements();
	}
	
	private void saveAlbums(Object inputList) {
		List<Album> list = new ArrayList<Album>();
		list = (List<Album>)inputList;
		theHubServer.updateAlbums(list);
		theHubServer.saveAlbums();
	}
	
	private void savePlaylists(Object inputList) {
		List<PlayList> list = new ArrayList<PlayList>();
		list = (List<PlayList>)inputList;
		theHubServer.updatePlaylists(list);
		theHubServer.savePlayLists();
	}
}
