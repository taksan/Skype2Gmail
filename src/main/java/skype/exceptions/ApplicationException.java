package skype.exceptions;

@SuppressWarnings("serial")
public class ApplicationException extends RuntimeException {

	public ApplicationException(Throwable e) {
		super(e);
	}

	public ApplicationException(String msg) {
		super(msg);
	}

}
