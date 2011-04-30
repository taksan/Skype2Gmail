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
import skype2gmail.mocks.FolderIndexMock;
import skype2gmail.mocks.SkypeMailMessageMock;

public class IndexedSkypeMailFolderTest {
	@Test
	public void testIndexedGmailFolder() {
		final FolderMock nonIndexedFolder = new FolderMock();
		FolderIndexMock folderIndex = new FolderIndexMock();
		folderIndex.addSignatureForId("#42$foo", "previous-signature");
		
		NonIndexGmailFolderProvider nonIndexedFolderMock = new NonIndexGmailFolderProvider() {
			@Override
			public SkypeMailFolder get() {
				return nonIndexedFolder;
			}
		}; 
		IndexedSkypeMailFolder indexedGmailFolder = new IndexedSkypeMailFolder(nonIndexedFolderMock, folderIndex);
				
		SkypeChat previousSkypeChat = SkypeApiMock.produceChatMock("#42$foo", "zoe", "joe");
		SkypeMailMessage gmailMessage = new SkypeMailMessageMock(previousSkypeChat);
		indexedGmailFolder.appendMessage(gmailMessage);
		
		SkypeChat otherPreviousSkypeChat = SkypeApiMock.produceChatMock("#is-not-indexed", "not", "indexed");
		SkypeMailMessage otherMessage = new SkypeMailMessageMock(otherPreviousSkypeChat);
		indexedGmailFolder.appendMessage(otherMessage);
		
		SkypeChat skypeChat = SkypeApiMock.produceChatMock("#42$foo", "zoe", "joe");
		SkypeMailMessage retrievedEntry = indexedGmailFolder.retrieveMessageEntryFor(skypeChat);
		Assert.assertTrue(Proxy.isProxyClass(retrievedEntry.getClass()));
		
		SkypeChat nonIndexedSkypeChat = SkypeApiMock.produceChatMock("#is-not-indexed", "not", "indexed");
		SkypeMailMessage entryForNonIndexedChat = indexedGmailFolder.retrieveMessageEntryFor(nonIndexedSkypeChat);
		
		Assert.assertFalse(Proxy.isProxyClass(entryForNonIndexedChat.getClass()));
		
		String[] posters = entryForNonIndexedChat.getPosters();
	
		String postersString = StringUtils.join(posters,",");
		Assert.assertEquals("indexed,not", postersString);
		
		String isNotIndexShouldBeIndexedAfterRetrieval = folderIndex.getSignatureFor("#is-not-indexed");
		
		Assert.assertEquals(nonIndexedSkypeChat.getBodySignature(), isNotIndexShouldBeIndexedAfterRetrieval);
		
		Assert.assertEquals(false, folderIndex.wasSaved());
		
		indexedGmailFolder.close();
		
		Assert.assertEquals(true, folderIndex.wasSaved());
	}
}
