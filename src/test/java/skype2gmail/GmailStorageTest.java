package skype2gmail;

import org.junit.Ignore;
import org.junit.Test;

import skype.SkypeChatDateFormat;
import skype.SkypeChatDateFormatImpl;
import skype.SkypeChatImpl;
import skype.SkypeChatSetter;
import skype.StorageEntry;
import skype.mocks.GmailMessageProviderMock;
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
		RootFolderProvider rootFolderProvider = new RootFolderProviderImpl(sessionProvider, userInfoProvider, skypeChatFolderProvider);
		GmailStorageEntryFactory entryFactory = new GmailStorageEntryFactoryImpl(skypeChatFormat,sessionProvider,rootFolderProvider);
		GmailMessageProvider messageProvider = new GmailMessageProviderMock(rootFolderProvider);
		
		GmailStorage gmailStorage = new GmailStorage(entryFactory, messageProvider);
		
		SkypeChatBuilderHelper chatHelper = new SkypeChatBuilderHelper() {

			@Override
			public void addChatMessages() {
				addMessage("joe", "Hya : ) ...", 21, 15, 14, 18);
				addMessage("moe", "fellow", 21, 15, 15, 18);
			}
		};
		SkypeChatImpl chat = chatHelper.getChat("$foo#too", "TOPIC_boo");
		StorageEntry newEntry = gmailStorage.newEntry(chat);
		newEntry.store(new SkypeChatSetter(chat));
	}
}
