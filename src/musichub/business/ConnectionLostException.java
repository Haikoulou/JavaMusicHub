package musichub.business;

import java.lang.Exception;

public class ConnectionLostException extends LogException {
	public ConnectionLostException(String msg) {
		super("CONFIG", msg);
	}
}