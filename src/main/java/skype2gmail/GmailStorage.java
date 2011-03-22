package skype2gmail;

import gmail.GmailMessage;
import skype.EmptySkypeChat;
import skype.SkypeChat;
import skype.SkypeStorage;
import skype.StorageEntry;

import com.google.inject.Inject;

public class GmailStorage implements SkypeStorage {

	private final GmailStorageEntryFactory entryFactory;
	private final GmailMessageProvider gmailMessageProvider;
	private final GmailMessageChatParser gmailMessageChatParser;
	private final GmailFolderStore rootFolderProvider;

	@Inject
	public GmailStorage(
			GmailStorageEntryFactory entryFactory,
			GmailMessageProvider gmailMessageProvider,
			GmailMessageChatParser gmailMessageChatParser, 
			GmailFolderStore rootFolderProvider) {
		this.entryFactory = entryFactory;
		this.gmailMessageProvider = gmailMessageProvider;
		this.gmailMessageChatParser = gmailMessageChatParser;
		this.rootFolderProvider = rootFolderProvider;
	}

	@Override
	public StorageEntry newEntry(SkypeChat chat) {
		return entryFactory.produce(chat);
	}

	@Override
	public StorageEntry retrievePreviousEntryFor(SkypeChat skypeChat) {
		GmailMessage[] storedMessages = getStoreMessages();
		for (GmailMessage message : storedMessages) {
			String chatId = message.getChatId();
			if (skypeChat.getId().equals(chatId)) {
				SkypeChat previousChat = makeSkypeChat(message);
				return entryFactory.produce(previousChat);
			}
		}
		return entryFactory.produce(new EmptySkypeChat());
	}

	private SkypeChat makeSkypeChat(GmailMessage message) {
		return gmailMessageChatParser.parse(message);
	}

	private GmailMessage[] getStoreMessages() {
		return gmailMessageProvider.getMessages();
	}

	@Override
	public void close() {
		rootFolderProvider.close();
	}
}
