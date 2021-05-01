package musichub.business;

import javax.sound.sampled.*;

public class AudioInputStreamCache {
	private AudioInputStream stream;
	private String name;
	
	public AudioInputStreamCache(AudioInputStream stream, String name) {
		this.stream = stream;
		this.name = name;
	}
	
	public AudioInputStream getStream() {
		return this.stream;
	}
	
	public String getName() {
		return this.name;
	}
}
