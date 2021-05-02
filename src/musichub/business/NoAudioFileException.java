package musichub.business;

import java.lang.Exception;

public class NoAudioFileException extends LogException {
	public NoAudioFileException(String msg) {
		super("WARNING", msg);
	}
}