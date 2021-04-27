package musichub.business;

import java.lang.Exception;

public class ConnectionFailureException extends Exception {
	public ConnectionFailureException(String msg) {
		super(msg);
	}
}
