package musichub.business;

public class NoAlbumFoundException extends LogException {

	public NoAlbumFoundException (String msg) {
		super("WARNING", msg);
	}
}