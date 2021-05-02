package musichub.business;


public class NoAlbumFoundException extends LogException {

	/**
	 * <b>NoAlbumFoundException constructor<b/>
	 * 
	 * this constructor creates an object msg 
	 * 
	 * @param msg: send a warning to the client if the album doesn't exist.
	 */
	public NoAlbumFoundException (String msg) {
		super("WARNING", msg);
	}
}