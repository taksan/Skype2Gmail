package skype2gmail;

import gmail.GmailMessage;
import gmail.mocks.FolderMock;

import javax.mail.internet.InternetAddress;

import junit.framework.Assert;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import skype.SkypeChat;
import skype.mocks.SkypeApiMock;
import skype2gmail.mocks.GmailMessageFactoryMock;
import skype2gmail.mocks.GmailMessageMock;
import utils.SimpleLoggerProvider;

public class FolderIndexImplTest {
	@Test
	public void testFolderIndexImpl() {
		
		SkypeChat skypeChat1 = SkypeApiMock.produceChatMock("#42$foo", "zoe", "joe","body-sig1");
		SkypeChat skypeChat2 = SkypeApiMock.produceChatMock("#43$foo", "zoe", "joe","body-sig2");
		String mockIndex = 
			makeEntry(skypeChat1)+"\r\n"+
			makeEntry(skypeChat2);
		
		
		FolderMock folderMock = new FolderMock(mockIndex);
		GmailMessageMock gmailMessageMock = new GmailMessageMock(skypeChat1);
		folderMock.appendMessage(gmailMessageMock);
		
		SimpleLoggerProvider loggerProvider = new SimpleLoggerProvider();
		GmailMessageFactoryMock gmailMessageFactory = new GmailMessageFactoryMock();
		FolderIndexImpl subject = new FolderIndexImpl(folderMock, gmailMessageFactory, loggerProvider);
		String actualForSkypeChat1 = subject.getSignatureFor("#42$foo");
		Assert.assertEquals(skypeChat1.getBodySignature(), actualForSkypeChat1);
		
		String actualForSkypeChat2 = subject.getSignatureFor("#43$foo");
		
		Assert.assertEquals(skypeChat2.getBodySignature(), actualForSkypeChat2);

		SkypeChat skypeChat3 = SkypeApiMock.produceChatMock("#44$foo", "zoe", "joe","body-sig3");
		subject.addIndexFor(skypeChat3);
		subject.save();
		
		GmailMessage indexMessage = folderMock.retrieveFirstMessageMatchingSearchTerm(FolderIndex.CHAT_INDEX_SEARCH_TERM);
		String updatedIndex = indexMessage.getBody();
		
		final String expectedIndex = mockIndex.replaceAll("\r", "")+"\n"+makeEntry(skypeChat3);
		
		Assert.assertEquals(expectedIndex, updatedIndex);
		
		InternetAddress[] senderAddress = indexMessage.getFrom();
		String senderName = StringUtils.join(senderAddress, "");
		Assert.assertEquals("Skype2Gmail", senderName);
		String topic = indexMessage.getTopic();
		Assert.assertEquals("Skype2Gmail chat index", topic);
	}

	private String makeEntry(SkypeChat skypeChat) {
		return String.format("%s,%s",skypeChat.getId(), skypeChat.getBodySignature());
	}
}
