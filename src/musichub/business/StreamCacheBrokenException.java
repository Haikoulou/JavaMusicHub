package musichub.business;

public class StreamCacheBrokenException extends LogException {

	public StreamCacheBrokenException (String msg) {
		super("WARNING", msg);
	}
}