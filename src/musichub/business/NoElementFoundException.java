package musichub.business;

import java.lang.Exception;

public class NoElementFoundException extends LogException {

	public NoElementFoundException (String msg) {
		super("WARNING", msg);
	}
}