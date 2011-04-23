package skype;


@SuppressWarnings("serial")
public class MessageProcessingException extends RuntimeException {

	public MessageProcessingException(String msg) {
		super(msg);
	}

	public MessageProcessingException(Throwable e) {
		super(e);
	}

}
