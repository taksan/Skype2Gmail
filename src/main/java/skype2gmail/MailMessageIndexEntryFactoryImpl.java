package skype2gmail;

import java.lang.reflect.Proxy;

import mail.SkypeMailFolder;
import mail.SkypeMailMessage;

import skype.SkypeChat;


public class MailMessageIndexEntryFactoryImpl implements MailMessageIndexEntryFactory {

	@Override
	public SkypeMailMessage produce(SkypeMailFolder nonIndexedGmailFolder, SkypeChat skypeChat, String previousChatSignature) {
		return (SkypeMailMessage) Proxy.newProxyInstance(
				SkypeMailMessage.class.getClassLoader(), 
				new Class<?>[]{SkypeMailMessage.class}, 
				new MailMessageIndexEntry(nonIndexedGmailFolder, skypeChat, previousChatSignature));
	}

}
