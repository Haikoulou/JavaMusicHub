package musichub.server;

import java.io.*;
import java.net.*;
import java.util.*;

//import javax.sound.sampled.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import musichub.business.Album;
import musichub.business.AudioBook;
import musichub.business.AudioElement;
import musichub.business.NoAlbumFoundException;
import musichub.business.NoElementFoundException;
import musichub.business.NoPlayListFoundException;
import musichub.business.PlayList;
import musichub.business.Song;
//import musichub.business.StreamNotFoundException;
import musichub.util.*;

/** 
 * <b>MusicHubServer Class</b> 
 * 
 *  Handle the MusicHub on the server.
 *  
 *  @author Elouan Toy
 *
 */ 

public class MusicHubServer extends Thread {
	private List<Album> albums;
	private List<PlayList> playlists;
	private List<AudioElement> elements;
	private ServerSocket ss;
	
	public static final String DIR = System.getProperty("user.dir");
	public static final String ALBUMS_FILE_PATH = DIR + "\\files\\albums.xml";
	public static final String PLAYLISTS_FILE_PATH = DIR + "\\files\\playlists.xml";
	public static final String ELEMENTS_FILE_PATH = DIR + "\\files\\elements.xml";
	public static final String AUDIOFILES_FILE_PATH = DIR + "\\files\\content\\";
	
	private XMLHandler xmlHandler = new XMLHandler();
	//private FileStreamHandler fileStreamHandler = new FileStreamHandler();
	
	/** 
	 * <b>MusicHubServer constructor</b> 
	 * 
	 *  Prepare the MusicHub by reading XML files and loading them in memory.
	 *
	 */ 
	
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
	
	/** 
	 * <b>MusicHubServer launch</b> 
	 * 
	 * Start the server and wait for a connection. Launch the ServerInstance in a new thread if a new client is connected.
	 * 
	 *  @param 
	 *  	int port: the port where the server should listen
	 *
	 */ 
	
	public void launch(int port) {
		try {
			ss = new ServerSocket(port);
			System.out.println("Server open on port " + port + ", waiting for connection...");
			while (true) {
				Socket socket = ss.accept();
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
	
	/** 
	 * <b>MusicHubServer close</b> 
	 * 
	 * Close the server.
	 * 
	 *
	 */ 
	
	public void close() {
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
	
	/** 
	 * <b>MusicHubServer getAudioStream</b> 
	 * 
	 * Start the server and wait for a connection. Launch the ServerInstance in a new thread if a new client is connected.
	 * 
	 *  @param 
	 *  	int port: the port where the server should listen
	 *
	
	
	public AudioInputStream getAudioStream(AudioElement element) throws StreamNotFoundException {
		AudioInputStream stream = null;
		for(AudioElement ae : elements) {
			if(ae.getUUID().equals(element.getUUID())) {
				stream = fileStreamHandler.getStreamFile(element.getContent());
			}
		}
		
		if(stream == null) throw new StreamNotFoundException("Incorrect audio file path (" + element.getContent() + ") of the title " + element.getTitle());
		return stream;
	} */
	
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
	
	/** 
	 * <b>MusicHubServer addElement</b> 
	 * 
	 * Add an audio element into the list.
	 * 
	 *  @param 
	 *  	AudioElement element: the AudioElement to add in the list.
	 *
	 */ 
	
	public void addElement(AudioElement element) {
		elements.add(element);
	}
	
	/** 
	 * <b>MusicHubServer addAlbum</b> 
	 * 
	 * Add an album into the list.
	 * 
	 *  @param 
	 *  	Album album: the album to add in the list.
	 *
	 */ 
	
	public void addAlbum(Album album) {
		albums.add(album);
	}
	
	/** 
	 * <b>MusicHubServer addEPlaylist</b> 
	 * 
	 * Add a playlist into the list.
	 * 
	 *  @param 
	 *  	PlayList playlist: the playlist to add in the list.
	 *
	 */ 
	
	public void addPlaylist(PlayList playlist) {
		playlists.add(playlist);
	}
	
	/** 
	 * <b>MusicHubServer updateElements</b> 
	 * 
	 * Replace the elements list with a new one.
	 * 
	 *  @param 
	 *  	List<AudioElement> list: the new list.
	 *
	 */ 
	
	public void updateElements(List<AudioElement> list) {
		elements = list;
	}
	
	/** 
	 * <b>MusicHubServer updateAlbums</b> 
	 * 
	 * Replace the albums list with a new one.
	 * 
	 *  @param 
	 *  	List<Album> list: the new list.
	 *
	 */ 
	
	public void updateAlbums(List<Album> list) {
		albums = list;
	}
	
	/** 
	 * <b>MusicHubServer updatePlaylists</b> 
	 * 
	 * Replace the playlists list with a new one.
	 * 
	 *  @param 
	 *  	List<PlayList> list: the new list.
	 *
	 */ 
	
	public void updatePlaylists(List<PlayList> list) {
		playlists = list;
	}
	
	/** 
	 * <b>MusicHubServer deletePlaylist</b> 
	 * 
	 * Delete a playlist from the list.
	 * 
	 *  @param 
	 *  	String playlistTitle: the name of the playlist to delete.
	 *  
	 *  @throws NoPlayListFoundException
	 *
	 */ 
	
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
	
	/** 
	 * <b>MusicHubServer addElementToAlbum</b> 
	 * 
	 * Add an audio element to an album.
	 * 
	 *  @param 
	 *  	String elementTitle: title of the audio element to add
	 *  	String albumTitle: title of the album
	 *  
	 *  @throws NoAlbumFoundException, NoElementFoundException
	 *
	 */ 
	
	public void addElementToAlbum(String elementTitle, String albumTitle) throws NoAlbumFoundException, NoElementFoundException
	{
		Album theAlbum = null;
		int i = 0;
		boolean found = false; 
		for (i = 0; i < albums.size(); i++) {
			if (albums.get(i).getTitle().toLowerCase().equals(albumTitle.toLowerCase())) {
				theAlbum = albums.get(i);
				found = true;
				break;
			}
		}

		if (found == true) {
			AudioElement theElement = null;
			for (AudioElement ae : elements) {
				if (ae.getTitle().toLowerCase().equals(elementTitle.toLowerCase())) {
					theElement = ae;
					break;
				}
			}
            if (theElement != null) {
                theAlbum.addSong(theElement.getUUID());
                //replace the album in the list
                albums.set(i,theAlbum);
            }
            else throw new NoElementFoundException("Element " + elementTitle + " not found!");
		}
		else throw new NoAlbumFoundException("Album " + albumTitle + " not found!");
		
	}
	
	/** 
	 * <b>MusicHubServer loadElements</b> 
	 * 
	 * Read elements from the XML file and load them into the elements list.
	 *
	 */ 
	
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
	
	/** 
	 * <b>MusicHubServer loadAlbums</b> 
	 * 
	 * Read albums from the XML file and load them into the albums list.
	 *
	 */ 
	
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
	
	/** 
	 * <b>MusicHubServer loadPlaylists</b> 
	 * 
	 * Read playlists from the XML file and load them into the playlists list.
	 *
	 */ 
	
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
	
	/** 
	 * <b>MusicHubServer saveAlbums</b> 
	 * 
	 * Write the content of the albums list into the XML file.
	 *
	 */ 
	
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
	
	/** 
	 * <b>MusicHubServer savePlaylists</b> 
	 * 
	 * Write the content of the playlists list into the XML file.
	 *
	 */ 
	
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
	
	/** 
	 * <b>MusicHubServer saveElements</b> 
	 * 
	 * Write the content of the audio elements list into the XML file.
	 *
	 */ 
	
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
