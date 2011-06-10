package skype2gmail;

import mail.SkypeMailFolder;
import mail.SkypeMailMessage;
import mail.SkypeMailMessageFactory;
import skype.commons.SkypeChat;
import skype.commons.SkypeChatDateFormat;
import skype.commons.SkypeChatFactory;
import skype.commons.StorageEntry;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class MailStorageEntryFactoryImpl implements MailStorageEntryFactory {

	private final SkypeChatDateFormat chatDateFormat;
	private final SkypeMailFolder gmailFolder;
	private final MailMessageChatParser gmailMessageChatParser;
	private final SkypeChatFactory skypeChatFactory;
	private final SkypeMailMessageFactory gmailMessageFactory;	
	
	@Inject
	public MailStorageEntryFactoryImpl(SkypeChatDateFormat chatDateFormat,
			SkypeMailFolder gmailFolder,
			MailMessageChatParser gmailMessageChatParser,
			SkypeChatFactory skypeChatFactory,
			SkypeMailMessageFactory gmailMessageFactory) {
		this.chatDateFormat = chatDateFormat;
		this.gmailMessageFactory = gmailMessageFactory;
		this.gmailFolder = gmailFolder;
		this.gmailMessageChatParser = gmailMessageChatParser;
		this.skypeChatFactory = skypeChatFactory;
	}

	@Override
	public StorageEntry produce(SkypeChat chat) {
		return new MailStorageEntry(gmailFolder, chat, this.chatDateFormat, gmailMessageFactory);
	}
	
	@Override
	public StorageEntry produce(SkypeChat newChat, SkypeMailMessage previousChatMessage) {
		if (previousChatMessage == null)
			return this.produce(this.skypeChatFactory.produceEmpty());
		
		SkypeChat previousChat = new LazySkypeChat(newChat, previousChatMessage, gmailMessageChatParser);
		return this.produce(previousChat);
	}

}
