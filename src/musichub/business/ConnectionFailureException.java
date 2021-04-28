package musichub.business;

import java.lang.Exception;

public class ConnectionFailureException extends LogException {
	public ConnectionFailureException(String msg) {
		super("CONFIG", msg);
	}
}