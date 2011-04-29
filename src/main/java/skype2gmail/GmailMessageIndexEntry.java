package skype2gmail;

import gmail.GmailMessage;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import skype.SkypeChat;

public class GmailMessageIndexEntry implements InvocationHandler {
	private final SkypeChat skypeChat;
	private GmailMessage actualMessage;
	private final GmailFolder actualFolder;
	private final String previousChatSignature;

	public GmailMessageIndexEntry(GmailFolder actualFolder, SkypeChat skypeChat, String previousChatSignature) {
		this.actualFolder = actualFolder;
		this.skypeChat = skypeChat;
		this.previousChatSignature = previousChatSignature;
		
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] paramArrayArgs)
			throws Throwable {
		if (method.getName().equals("getBodySignature")) {
			return previousChatSignature;
		}
		if (method.getName().equals("getId")) {
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

	GmailMessage getActualMessage() {
		return actualMessage;
	}

	private GmailMessage retrieveActualMessage() {
		return this.actualFolder.retrieveMessageEntryFor(skypeChat);
	}
}
