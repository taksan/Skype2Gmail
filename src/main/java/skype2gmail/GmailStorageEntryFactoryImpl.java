package skype2gmail;

import gmail.GmailMessage;
import skype.SkypeChat;
import skype.SkypeChatDateFormat;
import skype.SkypeChatFactory;
import skype.StorageEntry;

import com.google.inject.Inject;

public class GmailStorageEntryFactoryImpl implements GmailStorageEntryFactory {

	private final SkypeChatDateFormat chatDateFormat;
	private final SessionProvider sessionProvider;
	private final GmailFolder gmailFolder;
	private final GmailMessageChatParser gmailMessageChatParser;
	private final SkypeChatFactory skypeChatFactory;	
	
	@Inject
	public GmailStorageEntryFactoryImpl(SkypeChatDateFormat chatDateFormat,
			SessionProvider sessionProvider,
			GmailFolder gmailFolder,
			GmailMessageChatParser gmailMessageChatParser,
			SkypeChatFactory skypeChatFactory) {
		this.chatDateFormat = chatDateFormat;
		this.sessionProvider = sessionProvider;
		this.gmailFolder = gmailFolder;
		this.gmailMessageChatParser = gmailMessageChatParser;
		this.skypeChatFactory = skypeChatFactory;
	}

	@Override
	public StorageEntry produce(SkypeChat chat) {
		return new GmailStorageEntry(sessionProvider, gmailFolder, chat, this.chatDateFormat);
	}
	
	@Override
	public StorageEntry produce(SkypeChat newChat, GmailMessage previousChatMessage) {
		if (previousChatMessage == null)
			return this.produce(this.skypeChatFactory.produceEmpty());
		
		SkypeChat previousChat = new LazySkypeChat(newChat, previousChatMessage, gmailMessageChatParser);
		return this.produce(previousChat);
	}

}
