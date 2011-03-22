package skype2gmail;

import org.junit.Ignore;
import org.junit.Test;

import skype.SkypeChatDateFormat;
import skype.SkypeChatDateFormatImpl;
import skype.SkypeChatImpl;
import skype.SkypeChatSetter;
import skype.SkypeChatWithBodyParserFactory;
import skype.SkypeMessageDateFormat;
import skype.SkypeMessageDateFormatImpl;
import skype.SkypeUserFactory;
import skype.StorageEntry;
import skype.mocks.GmailMessageProviderMock;
import skype.mocks.SkypeUserFactoryMock;
import skype2disk.MessageBodyParserFactoryImpl;
import skype2gmail.mocks.MockAuthProvider;
import testutils.SkypeChatBuilderHelper;

@Ignore
public class GmailStorageTest {
	
	@Test
	public void testRetrieval() {
		UserAuthProvider userInfoProvider = new MockAuthProvider();
		SkypeChatFolderProvider skypeChatFolderProvider = new DefaultSkypeChatFolderProvider();
		SkypeChatDateFormat skypeChatFormat = new SkypeChatDateFormatImpl();
		SessionProvider sessionProvider = new SessionProviderImpl();
		GmailFolderStore rootFolderProvider = new GmailFolderStoreImpl(sessionProvider, userInfoProvider, skypeChatFolderProvider);
		GmailStorageEntryFactory entryFactory = new GmailStorageEntryFactoryImpl(skypeChatFormat,sessionProvider,rootFolderProvider);
		GmailMessageProvider messageProvider = new GmailMessageProviderMock(rootFolderProvider);
		SkypeMessageDateFormat skypeMessageDateFormat = new SkypeMessageDateFormatImpl();
		
		
		SkypeChatBuilderHelper chatHelper = new SkypeChatBuilderHelper() {
			
			@Override
			public void addChatMessages() {
				addMessage("joe", "Hya : ) ...", 21, 15, 14, 18);
				addMessage("moe", "fellow", 21, 15, 15, 18);
			}
		};
		SkypeUserFactory skypeUserFactory = new SkypeUserFactoryMock();
		MessageBodyParserFactoryImpl messageBodyParserFactory = 
			new MessageBodyParserFactoryImpl(chatHelper.skypeChatMessageFactory, skypeMessageDateFormat);
		
		SkypeChatWithBodyParserFactory skypeChatWithBodyParserFactory = new SkypeChatWithBodyParserFactory
			(chatHelper.skypeChatFactoryImpl, messageBodyParserFactory, skypeUserFactory);
		
		GmailMessageChatParser gmailMessageChatParser = new GmailMessageChatParser(skypeChatWithBodyParserFactory);
		GmailStorage gmailStorage = new GmailStorage(entryFactory, messageProvider, gmailMessageChatParser, null);
		SkypeChatImpl chat = chatHelper.getChat("$foo#too", "TOPIC_boo");
		StorageEntry newEntry = gmailStorage.newEntry(chat);
		newEntry.store(new SkypeChatSetter(chat));
	}
}
