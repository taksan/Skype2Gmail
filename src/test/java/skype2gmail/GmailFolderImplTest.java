package skype2gmail;

import gmail.GmailMessage;
import gmail.GmailMessageImpl;

import javax.mail.MessagingException;
import javax.mail.Session;

import junit.framework.Assert;

import org.junit.Test;

import skype.SkypeChat;
import skype.mocks.SkypeApiMock;
import skype2gmail.mocks.GmailMessageMock;
import skype2gmail.mocks.GmailStoreMock;

public class GmailFolderImplTest {
	@Test
	public void testAppendAndRetrieve() {
		GmailFolder subject = createSubject();

		String expectedChatId = "#foo$bar";
		SkypeChat skypeChat = SkypeApiMock.produceChatMock(expectedChatId,
				"joe", "moe");
		subject.appendMessage(new GmailMessageMock(skypeChat));

		GmailMessage retrievedMessage = subject.retrieveMessageEntryFor(skypeChat);

		String chatId = retrievedMessage.getChatId();
		Assert.assertEquals(expectedChatId, chatId);
	}

	@Test
	public void testDelete() throws MessagingException {
		GmailFolder subject = createSubject();

		String chatToDelete = "#foo$bar";
		SkypeChat skypeChat = SkypeApiMock.produceChatMock(chatToDelete, "joe", "moe");
		subject.appendMessage(new GmailMessageMock(skypeChat));

		subject.deleteMessageBasedOnId(chatToDelete);
		
		GmailMessage retrievedMessage = subject.retrieveMessageEntryFor(skypeChat);
		Assert.assertNull(retrievedMessage);
	}
	
	@Test
	public void testReplaceMessageMatchingTerm() {
		GmailFolder subject = createSubject();

		String chatToDelete = "#foo$bar";
		SkypeChat skypeChat = SkypeApiMock.produceChatMock(chatToDelete, "joe", "moe");
		subject.appendMessage(new GmailMessageMock(skypeChat));

		
		Session session = new SessionProviderImpl().getInstance();
		GmailMessage replacementMessage = new GmailMessageImpl(session);
		replacementMessage.setBody("replacement message");
		subject.replaceMessageMatchingTerm(null, replacementMessage);
		
		GmailMessage retrievedMessage = subject.retrieveMessageEntryFor(skypeChat);
		Assert.assertEquals(replacementMessage.getBody(), retrievedMessage.getBody());
	}

	private GmailFolder createSubject() {
		SkypeChatFolderProvider fp = new DefaultSkypeChatFolderProvider();
		GmailStore mockStore = new GmailStoreMock();
		return new GmailFolderImpl(fp, mockStore);
	}
}
