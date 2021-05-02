package musichub.business;

import java.util.*;
import java.io.File;

class SortByDate implements Comparator<Album>
{
	public int compare(Album a1, Album a2) {
			return a1.getDate().compareTo(a2.getDate());
	} 
}

class SortByGenre implements Comparator<Song>
{
	public int compare(Song s1, Song s2) {
			return s1.getGenre().compareTo(s2.getGenre());
	} 
}

class SortByAuthor implements Comparator<AudioElement>
{
	public int compare(AudioElement e1, AudioElement e2) {
			return e1.getArtist().compareTo(e2.getArtist());
	} 
}
	
public class MusicHub {
	private List<Album> albums;
	private List<PlayList> playlists;
	private List<AudioElement> elements;
	public MusicHandler music;
	
	private ServerConnection conn;
	
	public MusicHub () throws ConnectionFailureException {
		this.albums = new LinkedList<Album>();
		this.playlists = new LinkedList<PlayList>();
		this.elements = new LinkedList<AudioElement>();
		this.conn = new ServerConnection("localhost", 6667);
		
		
		System.out.println("Requesting data...");
		this.loadAudioElementsServer();
		this.loadAlbumsServer();
		this.loadPlaylistsServer();
		
		System.out.println("Successfully received " + this.elements.size() + " elements, " + this.albums.size() + " albums and " + this.playlists.size() + " playlists.");
		
		this.music = new MusicHandler();
	}
	
	public void downloadAudioFileServer(AudioElement element) throws StreamNotFoundException{
		boolean success = false;
		try {
			success = conn.getAudioFile(element);
		} catch (ConnectionLostException cle) {
			cle.printStackTrace();
		}
		
		if(success == false) throw new StreamNotFoundException("Impossible to find the title' stream from the server.");
	}
	
	public boolean checkAudioFile(AudioElement element) {
		File tmpDir = new File("tmp/audio/" + element.getUUID().toString());
		return tmpDir.exists();
	}
	
	public void clearCache() {
		File cache = new File("tmp/audio");
		File[] files = cache.listFiles();
		if(files != null) {
			for(File f : files) {
				f.delete();
			}
		}
	}
	
	public void updateElementsServer() {
		try {
			conn.sendElements(elements);
		} catch (ConnectionLostException cle) {
			cle.printStackTrace();
		}
	}
	
	public void updateAlbumsServer() {
		try {
			conn.sendAlbums(albums);
		} catch (ConnectionLostException cle) {
			cle.printStackTrace();
		}
	}
	
	public void updatePlaylistsServer() {
		try {
			conn.sendPlaylists(playlists);
		} catch (ConnectionLostException cle) {
			cle.printStackTrace();
		}
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
	
	public Iterator<Album> albums() { 
		return albums.listIterator();
	}
	
	public Iterator<PlayList> playlists() { 
		return playlists.listIterator();
	}
	
	public Iterator<AudioElement> elements() { 
		return elements.listIterator();
	}
	
	public String getAlbumsTitlesSortedByDate() {
		StringBuffer titleList = new StringBuffer();
		Collections.sort(albums, new SortByDate());
		for (Album al : albums)
			titleList.append(" - " + al.toString()+ "\n");
		return titleList.toString();
	}
	
	public String getAudiobooksTitlesSortedByAuthor() {
		StringBuffer titleList = new StringBuffer();
		List<AudioElement> audioBookList = new ArrayList<AudioElement>();
		for (AudioElement ae : elements)
				if (ae instanceof AudioBook)
					audioBookList.add(ae);
		Collections.sort(audioBookList, new SortByAuthor());
		for (AudioElement ab : audioBookList)
			titleList.append(" - " + ab.toString());
		return titleList.toString();
	}

	public String getAlbumSongs (String albumTitle) throws NoAlbumFoundException {
		Album theAlbum = null;
		String finalString = new String();
		int count = 1;
		for (Album al : albums) {
			if (al.getTitle().toLowerCase().equals(albumTitle.toLowerCase())) {
				theAlbum = al;
				break;
			}
		}
		if (theAlbum == null) throw new NoAlbumFoundException("No album with this title in the MusicHub!");

		List<UUID> songIDs = theAlbum.getSongs();
		for (UUID id : songIDs)
			for (AudioElement el : elements) {
				if (el instanceof Song) {
					//if (el.getUUID().equals(id)) songsInAlbum.add(el);
					if (el.getUUID().equals(id)) {
						finalString = finalString + " " + count + " - " + el.toString();
						count++;
					}
				}
			}
		return finalString;		
		
	}
	
	public List<Song> getAlbumSongsSortedByGenre (String albumTitle) throws NoAlbumFoundException {
		Album theAlbum = null;
		ArrayList<Song> songsInAlbum = new ArrayList<Song>();
		for (Album al : albums) {
			if (al.getTitle().toLowerCase().equals(albumTitle.toLowerCase())) {
				theAlbum = al;
				break;
			}
		}
		if (theAlbum == null) throw new NoAlbumFoundException("No album with this title in the MusicHub!");

		List<UUID> songIDs = theAlbum.getSongs();
		for (UUID id : songIDs)
			for (AudioElement el : elements) {
				if (el instanceof Song) {
					if (el.getUUID().equals(id)) songsInAlbum.add((Song)el);
				}
			}
		Collections.sort(songsInAlbum, new SortByGenre());
		return songsInAlbum;		
		
	}

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
	
	public void addElementToPlayList(String elementTitle, String playListTitle) throws NoPlayListFoundException, NoElementFoundException
	{
		PlayList thePlaylist = null;
        int i = 0;
		boolean found = false; 
		
        for (i = 0; i < playlists.size(); i++) {
			if (playlists.get(i).getTitle().toLowerCase().equals(playListTitle.toLowerCase())) {
				thePlaylist = playlists.get(i);
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
                thePlaylist.addElement(theElement.getUUID());
                //replace the album in the list
                playlists.set(i,thePlaylist);
            }
            else throw new NoElementFoundException("Element " + elementTitle + " not found!");
			
		} else throw new NoPlayListFoundException("Playlist " + playListTitle + " not found!");
		
	}
	
	public String getUuidByName(String name) throws NoElementFoundException {
		AudioElement element = null;
		for(AudioElement e : elements) {
			if(e.getTitle() == name) {
				element = e;
				break;
			}
		}
		
		if(element == null) throw new NoElementFoundException("Title not found!");
		return element.getUUID().toString();
	}
	
	public AudioElement getAudioElementByName(String name) throws NoElementFoundException {
		AudioElement element = null;
		for(AudioElement e : elements) {
			if(e.getTitle().toLowerCase().equals(name.toLowerCase())) {
				element = e;
				break;
			}
		}
		
		if(element == null) throw new NoElementFoundException("There is no AudioElement with the title " + name + "!");
		return element;
	}
	
	private void loadAudioElementsServer() {
		List<AudioElement> list = new ArrayList<>();
		try {
			list = conn.requestAudioElements();
		} catch (ConnectionLostException cle) {
			System.out.println("Impossible de charger les éléments audios.");
		}
		if (list.isEmpty()) return;
		
		for (AudioElement elementList : list) { //Read the new list to store it in memory
			this.addElement(elementList);
		}
	}
	
	private void loadPlaylistsServer() {
		List<PlayList> list = new ArrayList<>();
		try {
			list = conn.requestPlaylists();
		} catch (ConnectionLostException cle) {
			System.out.println("Impossible de charger les playlists.");
		}
		 //Request list of songs from the server
		if (list.isEmpty()) return;
		
		for (PlayList playlistList : list) { //Read the new list to store it in memory
			this.addPlaylist(playlistList);
		}
	}
	
	private void loadAlbumsServer() {
		List<Album> list = new ArrayList<>();
		try {
			list = conn.requestAlbums();
		} catch (ConnectionLostException cle) {
			System.out.println("Impossible de charger les albums.");
		}
		if (list.isEmpty()) return;
		
		for (Album albumList : list) { //Read the new list to store it in memory
			this.addAlbum(albumList);
		}
	}	
}