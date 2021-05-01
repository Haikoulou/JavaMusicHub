package musichub.business;

import java.io.File;
import java.util.*;
import javax.sound.sampled.*;
import javax.swing.JOptionPane;

public class MusicHandler {
	
	private Clip clip;
	private ArrayList<AudioInputStreamCache> cache;
	
	
    /** 
     * <b>MusicHandler constructor</b> 
     * 
     *  Prepare the MusicHandler to load streams.
     * 
     */ 
	public MusicHandler() {
		this.cache = new ArrayList<AudioInputStreamCache>();
		try {
			clip = AudioSystem.getClip();
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error : can't prepare MusicHandler");
		}
	}
	
	/** 
     * <b>MusicHandler loadFile</b> 
     * 
     *  Load the stream into the clip from a file
     * 
     * @param String filepath : 
     *     path to the .wav file that will be loaded
     */ 	
	public void loadFile(String filepath) {
		try {
			if(checkCache(filepath)) {
				clip.open(getCacheStream(filepath));
			}
			else {
				File file = new File(filepath);
				AudioInputStream stream = AudioSystem.getAudioInputStream(file);
				clip.open(stream);
				addCache(filepath, stream);
			}
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error : can't load music");
		}
	}
	
	/** 
     * <b>MusicHandler loadStream</b> 
     * 
     *  Load the stream into the clip from a stream
     * 
     * @param AudioInputStream stream : 
     *     stream of the title
     */ 	
	public void loadStream(AudioInputStream stream) {
		try {
			clip.open(stream);
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error : can't load music");
		}
	}
	
	/** 
     * <b>MusicHandler checkCache</b> 
     * 
     *  Returns if the title is already loaded in the cache
     * 
     * @param String name : 
     *     name of the file or uuid of the title
     */ 	
	public boolean checkCache(String name) {
		for(AudioInputStreamCache aisc : cache) {
			if(aisc.getName() == name)
				return true;
		}
		return false;
	}
	
	/** 
     * <b>MusicHandler checkCache</b> 
     * 
     *  Returns the stream by its id in the cache
     * 
     * @param String name : 
     *     name of the file or uuid of the title
     */ 	
	public AudioInputStream getCacheStream(String name) throws StreamCacheBrokenException {
		AudioInputStream streamToReturn = null;
		for(AudioInputStreamCache aisc : cache) {
			if(aisc.getName() == name) {
				streamToReturn = aisc.getStream();
			}
		}
		
		if(streamToReturn == null) throw new StreamCacheBrokenException("The stream cannot be found in the cache. Please restart the program if the error persits.");
		return streamToReturn;
	}
	
	/** 
     * <b>MusicHandler addCache</b> 
     * 
     *  Store a stream into the cache
     * 
     * @param String name : 
     *     name of the file or uuid of the title
     * @param AudioInputStream stream : 
     *     stream of the title
     */ 	
	public void addCache(String name, AudioInputStream stream) {
		AudioInputStreamCache newElement = new AudioInputStreamCache(stream, name);
		this.cache.add(newElement);
	}
	
    /** 
     * <b>MusicHandler rewind</b> 
     * 
     *  Sets the audio back by 5 seconds
     * 
     */
	public void rewind() {
		try{
			long position = clip.getMicrosecondPosition();
			clip.setMicrosecondPosition(position-5000000);
		} catch(Exception e) {
			//TODO: handle exception
		}
	}
	
    /** 
     * <b>MusicHandler fastForward</b> 
     * 
     *  Sets the audio forward by 5 seconds
     * 
     */
	public void fastForward() {
		try{
			long position = clip.getMicrosecondPosition();
			clip.setMicrosecondPosition(position+5000000);
		} catch(Exception e) {
			//TODO: handle exception
		}
	}
	
    /** 
     * <b>MusicHandler play</b> 
     * 
     *  Plays the audio if paused, does nothing if the audio is already playing
     * 
     */
	public void play() {
		try {
			clip.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
    /** 
     * <b>MusicHandler pause</b> 
     * 
     *  Pauses the lecture of the audio
     * 
     */
	public void pause() {
		try {
			clip.stop();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
    /** 
     * <b>MusicHandler reset</b> 
     * 
     *  Audio starts over from the beginning 
     * 
     */
	public void reset() {
		try {
			clip.setMicrosecondPosition(0);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
    /** 
     * <b>MusicHandler close</b> 
     * 
     *  closes the audio clip object
     * 
     */
	public void close() {
		try {
			clip.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/** 
     * <b>MusicHandler isRunning</b> 
     * 
     *  returns if a title is being played or not
     * 
     */	
	public boolean isRunning() {
		return clip.isRunning();
	}
}
