package musichub.business;

public class IncorrectAudioFormatException extends LogException {

	public IncorrectAudioFormatException (String msg) {
		super("WARNING", msg);
	}
}