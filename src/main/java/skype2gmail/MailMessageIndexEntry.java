package skype2gmail;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import mail.SkypeMailFolder;
import mail.SkypeMailMessage;

import skype.SkypeChat;

public class MailMessageIndexEntry implements InvocationHandler {
	private final SkypeChat skypeChat;
	private SkypeMailMessage actualMessage;
	private final SkypeMailFolder actualFolder;
	private final String previousChatSignature;

	public MailMessageIndexEntry(SkypeMailFolder actualFolder, SkypeChat skypeChat, String previousChatSignature) {
		this.actualFolder = actualFolder;
		this.skypeChat = skypeChat;
		this.previousChatSignature = previousChatSignature;
		
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] paramArrayArgs)
			throws Throwable {
		if (method.getName().equals("getBodySignature")) {
			if (actualMessage == null)
				return previousChatSignature;
		}
		if (method.getName().equals("getId")) {
			if (actualMessage == null) 
				return skypeChat.getId();
		}
		if (actualMessage == null) {
			actualMessage = retrieveActualMessage();
		}
		try {
			return method.invoke(actualMessage, paramArrayArgs);
		}catch(InvocationTargetException ex) {
			throw ex.getCause();
		}
	}

	SkypeMailMessage getActualMessage() {
		return actualMessage;
	}

	private SkypeMailMessage retrieveActualMessage() {
		return this.actualFolder.retrieveMessageEntryFor(skypeChat);
	}
}
