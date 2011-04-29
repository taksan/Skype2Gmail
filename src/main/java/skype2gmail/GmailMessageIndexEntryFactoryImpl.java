package skype2gmail;

import java.lang.reflect.Proxy;

import skype.SkypeChat;

import gmail.GmailMessage;

public class GmailMessageIndexEntryFactoryImpl implements GmailMessageIndexEntryFactory {

	@Override
	public GmailMessage produce(GmailFolder nonIndexedGmailFolder, SkypeChat skypeChat, String previousChatSignature) {
		return (GmailMessage) Proxy.newProxyInstance(
				GmailMessage.class.getClassLoader(), 
				new Class<?>[]{GmailMessage.class}, 
				new GmailMessageIndexEntry(nonIndexedGmailFolder, skypeChat, previousChatSignature));
	}

}
