package musichub.business;

import java.lang.Exception;

public class NoElementFoundException extends LogException {
	/**
	 * <b>NoElementFoundException constructor<b/>
	 * 
	 * this constructor creates an object msg 
	 * 
	 * @param msg: send a warning to the client if the element doesn't exist.
	 */

	public NoElementFoundException (String msg) {
		super("WARNING", msg);
	}
}