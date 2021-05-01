package musichub.business;

public class StreamNotFoundException extends LogException {

	public StreamNotFoundException (String msg) {
		super("WARNING", msg);
	}
}