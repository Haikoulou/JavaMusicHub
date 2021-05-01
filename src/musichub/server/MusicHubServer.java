package musichub.server;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.sound.sampled.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import musichub.business.Album;
import musichub.business.AudioBook;
import musichub.business.AudioElement;
import musichub.business.NoPlayListFoundException;
import musichub.business.PlayList;
import musichub.business.Song;
import musichub.business.StreamNotFoundException;
import musichub.util.*;

public class MusicHubServer {
	private List<Album> albums;
	private List<PlayList> playlists;
	private List<AudioElement> elements;
	private ServerSocket ss;
	
	public static final String DIR = System.getProperty("user.dir");
	public static final String ALBUMS_FILE_PATH = DIR + "\\files\\albums.xml";
	public static final String PLAYLISTS_FILE_PATH = DIR + "\\files\\playlists.xml";
	public static final String ELEMENTS_FILE_PATH = DIR + "\\files\\elements.xml";
	
	private XMLHandler xmlHandler = new XMLHandler();
	
	public MusicHubServer() {
		//prepare lists for elements
		this.albums = new LinkedList<Album>();
		this.playlists = new LinkedList<PlayList>();
		this.elements = new LinkedList<AudioElement>();
		
		//load elements from xml files
		this.loadElements();
		this.loadAlbums();
		this.loadPlaylists();
		
		System.out.println("Successfully loaded " + this.elements.size() + " elements, " + this.albums.size() + " albums and " + this.playlists.size() + " playlists.");
	}
	
	//Server management
	
	public void launch(int port) {
		try {
			ss = new ServerSocket(port);
			System.out.println("Server waiting for connection...");
			while (true) {
				Socket socket = ss.accept();
				System.out.println("Connected to " + socket.getRemoteSocketAddress().toString());
				new ServerInstance(socket, this).start();
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
	
	public void stop() {
		if (ss != null && !ss.isClosed()) {
			System.out.println("Closing connection...");
			try {
				ss.close();
			} catch (IOException e) {
				e.printStackTrace(System.err);
			}
		}
	}
	
	//Elements management
	
	public AudioInputStream getAudioStream(AudioElement element) throws StreamNotFoundException {
		AudioInputStream stream = null;
		System.out.println(element.getContent());
		for(AudioElement ae : elements) {
			if(ae.equals(element)) {
				stream = new FileStreamHandler().getStreamFile(element.getContent());
			}
		}
		
		if(stream == null) throw new StreamNotFoundException("Incorrect audio file path (" + element.getContent() + ") of the title " + element.getTitle());
		return stream;
	}
	
	public Iterator<Album> albums() { 
		return albums.listIterator();
	}
	
	public Iterator<PlayList> playlists() { 
		return playlists.listIterator();
	}
	
	public Iterator<AudioElement> elements() { 
		return elements.listIterator();
	}
	
	public List<AudioElement> getAudioElements(){
		return this.elements;
	}
	
	public List<Album> getAlbums(){
		return this.albums;
	}
	
	public List<PlayList> getPlaylists(){
		return this.playlists;
	}
	
	public void addElement(AudioElement element) {
		elements.add(element);
	}
	
	public void addAlbum(Album album) {
		albums.add(album);
	}
	
	public void addPlaylist(PlayList playlist) {
		playlists.add(playlist);
	}
	
	public void updateElements(List<AudioElement> list) {
		elements = list;
	}
	
	public void updateAlbums(List<Album> list) {
		albums = list;
	}
	
	public void updatePlaylists(List<PlayList> list) {
		playlists = list;
	}
	
	public void deletePlayList(String playListTitle) throws NoPlayListFoundException {
		
		PlayList thePlayList = null;
		boolean result = false;
		for (PlayList pl : playlists) {
			if (pl.getTitle().toLowerCase().equals(playListTitle.toLowerCase())) {
				thePlayList = pl;
				break;
			}
		}
		
		if (thePlayList != null)  		
			result = playlists.remove(thePlayList); 
		if (!result) throw new NoPlayListFoundException("Playlist " + playListTitle + " not found!");
	}
	
	private void loadElements () {
		NodeList audioelementsNodes = xmlHandler.parseXMLFile(ELEMENTS_FILE_PATH);
		if (audioelementsNodes == null) return;
		
		for (int i = 0; i < audioelementsNodes.getLength(); i++) {
			if (audioelementsNodes.item(i).getNodeType() == Node.ELEMENT_NODE)   {
				Element audioElement = (Element) audioelementsNodes.item(i);
				if (audioElement.getNodeName().equals("song")) 	{
					try {
						AudioElement newSong = new Song (audioElement);
						this.addElement(newSong);
					} catch (Exception ex) 	{
						System.out.println ("Something is wrong with the XML song element");
					}
				}
				if (audioElement.getNodeName().equals("audiobook")) 	{
					try {
						AudioElement newAudioBook = new AudioBook (audioElement);
						this.addElement(newAudioBook);
					} catch (Exception ex) 	{
						System.out.println ("Something is wrong with the XML audiobook element");
					}
				}
			}  
		}
	}
	
	private void loadAlbums () {
		NodeList albumNodes = xmlHandler.parseXMLFile(ALBUMS_FILE_PATH);
		if (albumNodes == null) return;
				
		for (int i = 0; i < albumNodes.getLength(); i++) {
			if (albumNodes.item(i).getNodeType() == Node.ELEMENT_NODE)   {
				Element albumElement = (Element) albumNodes.item(i);
				if (albumElement.getNodeName().equals("album")) 	{
					try {
						this.addAlbum(new Album (albumElement));
					} catch (Exception ex) {
						System.out.println ("Something is wrong with the XML album element");
					}
				}
			}  
		}
	}
	
	private void loadPlaylists () {
		NodeList playlistNodes = xmlHandler.parseXMLFile(PLAYLISTS_FILE_PATH);
		if (playlistNodes == null) return;
		
		for (int i = 0; i < playlistNodes.getLength(); i++) {
			if (playlistNodes.item(i).getNodeType() == Node.ELEMENT_NODE)   {
				Element playlistElement = (Element) playlistNodes.item(i);
				if (playlistElement.getNodeName().equals("playlist")) 	{
					try {
						this.addPlaylist(new PlayList (playlistElement));
					} catch (Exception ex) {
						System.out.println ("Something is wrong with the XML playlist element");
					}
				}
			}  
		}
	}
	
	public void saveAlbums () {
		Document document = xmlHandler.createXMLDocument();
		if (document == null) return;
		
		// root element
		Element root = document.createElement("albums");
		document.appendChild(root);

		//save all albums
		for (Iterator<Album> albumsIter = this.albums(); albumsIter.hasNext();) {
			Album currentAlbum = albumsIter.next();
			currentAlbum.createXMLElement(document, root);
		}
		xmlHandler.createXMLFile(document, ALBUMS_FILE_PATH);
	}
	
	public void savePlayLists () {
		Document document = xmlHandler.createXMLDocument();
		if (document == null) return;
		
		// root element
		Element root = document.createElement("playlists");
		document.appendChild(root);

		//save all playlists
		for (Iterator<PlayList> playlistsIter = this.playlists(); playlistsIter.hasNext();) {
			PlayList currentPlayList = playlistsIter.next();
			currentPlayList.createXMLElement(document, root);
		}
		xmlHandler.createXMLFile(document, PLAYLISTS_FILE_PATH);
	}
	
	public void saveElements() {
		Document document = xmlHandler.createXMLDocument();
		if (document == null) return;

		// root element
		Element root = document.createElement("elements");
		document.appendChild(root);

		//save all AudioElements
		Iterator<AudioElement> elementsIter = elements.listIterator(); 
		while (elementsIter.hasNext()) {
			
			AudioElement currentElement = elementsIter.next();
			if (currentElement instanceof Song)
			{
				((Song)currentElement).createXMLElement(document, root);
			}
			if (currentElement instanceof AudioBook)
			{ 
				((AudioBook)currentElement).createXMLElement(document, root);
			}
		}
		xmlHandler.createXMLFile(document, ELEMENTS_FILE_PATH);
 	}
}
