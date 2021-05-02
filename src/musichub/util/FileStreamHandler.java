package musichub.util;

import java.io.File;

import javax.sound.sampled.*;

public class FileStreamHandler {
	public FileStreamHandler() {
		try {
			Clip clip = AudioSystem.getClip();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public AudioInputStream getStreamFile(String pathfile) {
		AudioInputStream stream = null;
		try {
			File file = new File(pathfile);
			stream = AudioSystem.getAudioInputStream(file);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return stream;
	}
}