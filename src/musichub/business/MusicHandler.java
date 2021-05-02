package musichub.business;

import java.io.File;

import javax.sound.sampled.*;
import javax.swing.JOptionPane;

public class MusicHandler {
	
	private Clip clip;
	
	
    /** 
     * <b>MusicHandler constructor</b> 
     * 
     *  Prepare the MusicHandler to load streams.
     * 
     */ 
	public MusicHandler() {
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
	public void loadAudioElement(AudioElement ae) throws IncorrectAudioFormatException {
		if(!ae.getFormat().equals("wav")) throw new IncorrectAudioFormatException("This player can only play WAV files (for the moment UwU)");
		try {
			clip.close();
			File file = new File("tmp/audio/" + ae.getUUID().toString() + "." + ae.getFormat());
			AudioInputStream stream = AudioSystem.getAudioInputStream(file);
			clip.open(stream);
		} catch (Exception ioe) {
			System.out.println(ioe);
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
			clip.close();
			File file = new File(filepath);
			AudioInputStream stream = AudioSystem.getAudioInputStream(file);
			clip.open(stream);
		} catch (Exception ioe) {
			System.out.println(ioe);
		}
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
