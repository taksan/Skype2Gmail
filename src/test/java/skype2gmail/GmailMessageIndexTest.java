package skype2gmail;

import gmail.GmailMessage;

import java.lang.reflect.Proxy;

import junit.framework.Assert;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import skype.SkypeChat;
import skype.mocks.FolderMock;
import skype.mocks.SkypeApiMock;

public class GmailMessageIndexTest {
	@Test
	public void testLazyRehydration() {
		SkypeChat previousSkypeChat = SkypeApiMock.produceChatMock("#42$foo", "moe", "joe");
		
		GmailMessageIndexEntryFactoryImpl gmailMessageIndexEntryFactory = new GmailMessageIndexEntryFactoryImpl();
		
		GmailFolder nonIndexedGmailFolder = new FolderMock();
		GmailMessageMock gmailMessageMock = new GmailMessageMock(previousSkypeChat);
		nonIndexedGmailFolder.appendMessage(gmailMessageMock);
		
		
		SkypeChat skypeChat = SkypeApiMock.produceChatMock("#42$foo", "zoe", "joe");
		String previousChatSignature = "previous-signature";
		GmailMessage gmailMessage = gmailMessageIndexEntryFactory.produce(nonIndexedGmailFolder, skypeChat, previousChatSignature );
		
		String bodySignature = gmailMessage.getBodySignature();
		Assert.assertEquals(previousChatSignature, bodySignature);
		
		GmailMessageIndexEntry indexEntry = 
			(GmailMessageIndexEntry) Proxy.getInvocationHandler(gmailMessage);
		
		Assert.assertNull(indexEntry.getActualMessage());
		
		String[] posters = gmailMessage.getPosters();
		Assert.assertNotNull(indexEntry.getActualMessage());
		
		String postersString = StringUtils.join(posters,",");
		Assert.assertEquals("joe,moe", postersString);
	}
}
