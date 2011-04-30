package skype2gmail;

import java.util.Calendar;

import javax.mail.internet.InternetAddress;

import junit.framework.Assert;
import mail.SkypeMailMessage;
import mail.mocks.FolderMock;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import skype.SkypeChat;
import skype.mocks.SkypeApiMock;
import skype2gmail.mocks.SkypeMailMessageMock;
import skype2gmail.mocks.SkypeMimeMessageFactoryMock;
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
		SkypeMailMessageMock gmailMessageMock = new SkypeMailMessageMock(skypeChat1);
		folderMock.appendMessage(gmailMessageMock);
		SimpleLoggerProvider loggerProvider = new SimpleLoggerProvider();
		SkypeMimeMessageFactoryMock gmailMessageFactory = new SkypeMimeMessageFactoryMock();
		
		FolderIndexImpl subject = new FolderIndexImpl(folderMock, gmailMessageFactory, loggerProvider);
		
		String actualForSkypeChat1 = subject.getSignatureFor("#42$foo");
		Assert.assertEquals(skypeChat1.getBodySignature(), actualForSkypeChat1);
		
		String actualForSkypeChat2 = subject.getSignatureFor("#43$foo");
		Assert.assertEquals(skypeChat2.getBodySignature(), actualForSkypeChat2);

		SkypeChat skypeChat3 = SkypeApiMock.produceChatMock("#44$foo", "zoe", "joe","body-sig3");
		subject.addIndexFor(skypeChat3);
		subject.save();
		
		SkypeMailMessage indexMessage = folderMock.retrieveSingleMessageMatchingSearchTerm(FolderIndex.CHAT_INDEX_SEARCH_TERM);
		String updatedIndex = indexMessage.getBody();
		
		final String expectedIndex = mockIndex.replaceAll("\r", "")+"\n"+makeEntry(skypeChat3);
		Assert.assertEquals(expectedIndex, updatedIndex);
		
		InternetAddress[] senderAddress = indexMessage.getFrom();
		String senderName = StringUtils.join(senderAddress, "");
		Assert.assertEquals("Skype2Gmail", senderName);
		String topic = indexMessage.getTopic();
		Assert.assertEquals("Skype2Gmail chat index", topic);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(indexMessage.getDate());
		Assert.assertEquals(1942, cal.get(Calendar.YEAR));
	}

	private String makeEntry(SkypeChat skypeChat) {
		return String.format("%s,%s",skypeChat.getId(), skypeChat.getBodySignature());
	}
}
