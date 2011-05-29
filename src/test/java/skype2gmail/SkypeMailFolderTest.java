package skype2gmail;


import javax.mail.MessagingException;

import junit.framework.Assert;
import mail.SkypeMailFolder;
import mail.SkypeMailFolderImpl;
import mail.SkypeMailMessage;
import mail.SkypeMailMessageFactoryImpl;

import org.junit.Before;
import org.junit.Test;

import skype.commons.SkypeChat;
import skype.mocks.SkypeApiMock;
import skype2gmail.mocks.SkypeMailMessageMock;
import skype2gmail.mocks.SkypeMailStoreMock;
import utils.SimpleLoggerProvider;

public class SkypeMailFolderTest {
	private SkypeMailStoreMock mockStore;
	
	@Before
	public void before(){
		mockStore = new SkypeMailStoreMock();
	}

	@Test
	public void testAppendAndRetrieve() {
		SkypeMailFolder subject = createSubject();

		String expectedChatId = "#foo$bar";
		SkypeChat skypeChat = SkypeApiMock.produceChatMock(expectedChatId,
				"joe", "moe");
		SkypeMailMessageMock mailMessageMock = new SkypeMailMessageMock(skypeChat);
		subject.appendMessage(mailMessageMock);

		SkypeMailMessage retrievedMessage = subject.retrieveMessageEntryFor(skypeChat);

		String chatId = retrievedMessage.getChatId();
		Assert.assertEquals(expectedChatId, chatId);
	}
	
	@Test
	public void testClose() {
		SkypeMailFolder subject = createSubject();

		SkypeChat skypeChat = SkypeApiMock.produceChatMock("#foo$bar", "joe", "moe");
		SkypeMailMessageMock mailMessageMock = new SkypeMailMessageMock(skypeChat);
		subject.appendMessage(mailMessageMock);

		subject.close();
		Assert.assertTrue(mockStore.closeWasInvoked);
		Assert.assertFalse(mockStore.javaMailFolderMock.isOpen());
	}

	@Test
	public void testDelete() throws MessagingException {
		SkypeMailFolder subject = createSubject();

		String chatToDelete = "#foo$bar";
		SkypeChat skypeChat = SkypeApiMock.produceChatMock(chatToDelete, "joe", "moe");
		subject.appendMessage(new SkypeMailMessageMock(skypeChat));

		subject.deleteMessageBasedOnId(chatToDelete);
		
		SkypeMailMessage retrievedMessage = subject.retrieveMessageEntryFor(skypeChat);
		Assert.assertNull(retrievedMessage);
	}
	
	@Test
	public void testReplaceMessageMatchingTerm() {
		SkypeMailFolder subject = createSubject();

		String chatToDelete = "#foo$bar";
		SkypeChat skypeChat = SkypeApiMock.produceChatMock(chatToDelete, "joe", "moe");
		subject.appendMessage(new SkypeMailMessageMock(skypeChat));

		
		SkypeMailMessageFactoryImpl gmailMessageFactoryImpl = getGmailMessageFactory();
		SkypeMailMessage replacementMessage = gmailMessageFactoryImpl.factory();
		replacementMessage.setBody("replacement message");
		subject.replaceMessageMatchingTerm(null, replacementMessage);
		
		SkypeMailMessage retrievedMessage = subject.retrieveMessageEntryFor(skypeChat);
		Assert.assertEquals(replacementMessage.getBody(), retrievedMessage.getBody());
	}

	private SkypeMailMessageFactoryImpl getGmailMessageFactory() {
		SessionProviderImpl sessionProvider = new SessionProviderImpl();
		DefaultSkypeChatFolderProvider cfp = new DefaultSkypeChatFolderProvider();
		SimpleLoggerProvider loggerProvider = new SimpleLoggerProvider();
		
		SkypeMailMessageFactoryImpl gmailMessageFactoryImpl = 
			new SkypeMailMessageFactoryImpl(sessionProvider, cfp, mockStore, loggerProvider);
		return gmailMessageFactoryImpl;
	}

	private SkypeMailFolder createSubject() {
		SkypeChatFolderProvider fp = new DefaultSkypeChatFolderProvider();
		return new SkypeMailFolderImpl(fp, mockStore, getGmailMessageFactory());
	}
}
