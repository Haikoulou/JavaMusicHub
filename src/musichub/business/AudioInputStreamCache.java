package musichub.business;

import java.io.IOException;

import javax.sound.sampled.*;

public class AudioInputStreamCache {
	private AudioInputStream stream;
	private String name;
	
	public AudioInputStreamCache(AudioInputStream stream, String name) {
		this.stream = stream;
		this.name = name;
	}
	
	public AudioInputStream getStream() {
		try {
			this.stream.reset();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return this.stream;
	}
	
	public String getName() {
		return this.name;
	}
}
