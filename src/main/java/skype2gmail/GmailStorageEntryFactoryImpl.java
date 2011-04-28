package skype2gmail;

import gmail.GmailMessageInterface;

import com.google.inject.Inject;

import skype.SkypeChat;
import skype.SkypeChatDateFormat;
import skype.SkypeChatFactory;
import skype.StorageEntry;

public class GmailStorageEntryFactoryImpl implements GmailStorageEntryFactory {

	private final SkypeChatDateFormat chatDateFormat;
	private final SessionProvider sessionProvider;
	private final GmailFolder rootFolderProvider;
	private final GmailMessageChatParser gmailMessageChatParser;
	private final SkypeChatFactory skypeChatFactory;	
	
	@Inject
	public GmailStorageEntryFactoryImpl(SkypeChatDateFormat chatDateFormat,
			SessionProvider sessionProvider,
			GmailFolder rootFolderProvider,
			GmailMessageChatParser gmailMessageChatParser,
			SkypeChatFactory skypeChatFactory) {
		this.chatDateFormat = chatDateFormat;
		this.sessionProvider = sessionProvider;
		this.rootFolderProvider = rootFolderProvider;
		this.gmailMessageChatParser = gmailMessageChatParser;
		this.skypeChatFactory = skypeChatFactory;
	}

	@Override
	public StorageEntry produce(SkypeChat chat) {
		return new GmailStorageEntry(sessionProvider, rootFolderProvider, chat, this.chatDateFormat);
	}
	
	@Override
	public StorageEntry produce(SkypeChat newChat, GmailMessageInterface previousChatMessage) {
		if (previousChatMessage == null)
			return this.produce(this.skypeChatFactory.produceEmpty());
		
		SkypeChat previousChat = new LazySkypeChat(newChat, previousChatMessage, gmailMessageChatParser);
		return this.produce(previousChat);
	}

}
