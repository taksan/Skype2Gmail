package skype.commons;

import skype.exceptions.MessageProcessingException;


@SuppressWarnings("serial")
public class SkypeMessageParsingException extends MessageProcessingException {
	public SkypeMessageParsingException(String message, Object ... args)
	{
		super(String.format(message,args));
	}
}
