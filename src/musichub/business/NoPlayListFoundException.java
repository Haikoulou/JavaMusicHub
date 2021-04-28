package musichub.business;

import java.lang.Exception;

public class NoPlayListFoundException extends LogException {

	public NoPlayListFoundException (String msg) {
		super("WARNING", msg);
	}
}