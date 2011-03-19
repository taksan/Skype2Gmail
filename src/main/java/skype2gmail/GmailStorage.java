package skype2gmail;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.apache.commons.lang.NotImplementedException;

import skype.EmptySkypeChat;
import skype.SkypeChat;
import skype.SkypeStorage;
import skype.StorageEntry;

import com.google.inject.Inject;

public class GmailStorage implements SkypeStorage {

	public static final String X_MESSAGE_ID = "X-SKYPE-MESSAGE-ID";
	public static final String X_BODY_SIGNATURE = "X-SKYPE-BODY-SIGNATURE";
	public static final String X_MESSAGES_SIGNATURES = "X-SKYPE-MESSAGES-SIGNATURES";
	
	private final GmailStorageEntryFactory entryFactory;
	private final GmailMessageProvider gmailMessageProvider;
	private Message[] storedMessages;

	@Inject
	public GmailStorage(GmailStorageEntryFactory entryFactory, GmailMessageProvider gmailMessageProvider) {
		this.entryFactory = entryFactory;
		this.gmailMessageProvider = gmailMessageProvider;
	}

	@Override
	public StorageEntry newEntry(SkypeChat chat) {
		return entryFactory.produce(chat);
	}

	@Override
	public StorageEntry retrievePreviousEntryFor(SkypeChat skypeChat) {
		try {
			Message[] storedMessages = getStoreMessages();
			for (Message message : storedMessages) {
				String[] header = message.getHeader(X_MESSAGE_ID);
				if (header == null) 
					continue;
				if (skypeChat.getId().equals(header[0])) {
					SkypeChat previousChat = makeSkypeChat(message);
					return entryFactory.produce(previousChat);
				}
			}
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		return entryFactory.produce(new EmptySkypeChat());
	}

	private SkypeChat makeSkypeChat(Message message) {
		throw new NotImplementedException();
	}

	private Message[] getStoreMessages() throws MessagingException {
		return gmailMessageProvider.getMessages();
	}

}
