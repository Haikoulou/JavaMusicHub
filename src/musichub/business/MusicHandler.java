package musichub.business;

import java.io.File;
import java.util.Scanner;
import javax.sound.sampled.*;
import javax.swing.JOptionPane;

public class MusicHandler {
	
	private Clip clip;
	
	
    /** 
     * <b>MusicHandler constructor</b> 
     * 
     *  This constructor creates a Clip object and loads a .wav file in it
     * 
     * @param String filepath : 
     *     path to the .wav file that will be loaded
     */ 
	public MusicHandler(String filepath) {
		try {
			File file = new File(filepath);
			AudioInputStream stream = AudioSystem.getAudioInputStream(file);

			clip = AudioSystem.getClip();
			clip.open(stream);
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error : can't load music");
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
}
