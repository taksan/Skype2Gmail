package skype2gmail;

import java.lang.reflect.Proxy;

import junit.framework.Assert;
import mail.SkypeMailFolder;
import mail.SkypeMailMessage;
import mail.mocks.FolderMock;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import skype.SkypeChat;
import skype.mocks.SkypeApiMock;
import skype2gmail.mocks.SkypeMailMessageMock;

public class MailMessageIndexEntryTest {
	@Test
	public void testLazyRehydration() {
		SkypeChat previousSkypeChat = SkypeApiMock.produceChatMock("#42$foo", "moe", "joe");
		
		MailMessageIndexEntryFactoryImpl gmailMessageIndexEntryFactory = new MailMessageIndexEntryFactoryImpl();
		
		SkypeMailFolder nonIndexedGmailFolder = new FolderMock();
		SkypeMailMessageMock gmailMessageMock = new SkypeMailMessageMock(previousSkypeChat);
		nonIndexedGmailFolder.appendMessage(gmailMessageMock);
		
		
		SkypeChat skypeChat = SkypeApiMock.produceChatMock("#42$foo", "zoe", "joe");
		String previousChatSignature = "previous-signature";
		SkypeMailMessage gmailMessage = gmailMessageIndexEntryFactory.produce(nonIndexedGmailFolder, skypeChat, previousChatSignature );
		
		String bodySignature = gmailMessage.getBodySignature();
		Assert.assertEquals(previousChatSignature, bodySignature);
		
		MailMessageIndexEntry indexEntry = 
			(MailMessageIndexEntry) Proxy.getInvocationHandler(gmailMessage);
		
		Assert.assertNull(indexEntry.getActualMessage());
		
		String[] posters = gmailMessage.getPosters();
		Assert.assertNotNull(indexEntry.getActualMessage());
		
		String postersString = StringUtils.join(posters,",");
		Assert.assertEquals("joe,moe", postersString);
	}
}
