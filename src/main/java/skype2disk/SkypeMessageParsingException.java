package skype2disk;

@SuppressWarnings("serial")
public class SkypeMessageParsingException extends RuntimeException {
	public SkypeMessageParsingException(String message, Object ... args)
	{
		super(String.format(message,args));
	}
}
