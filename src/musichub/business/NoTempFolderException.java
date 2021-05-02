package musichub.business;

public class NoTempFolderException extends LogException {

	public NoTempFolderException (String msg) {
		super("WARNING", msg);
	}
}