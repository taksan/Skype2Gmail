package skype2gmail;

import gmail.GmailMessage;

import java.lang.reflect.Proxy;

import junit.framework.Assert;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import skype.SkypeChat;
import skype.mocks.FolderMock;
import skype.mocks.SkypeApiMock;

public class IndexedGmailFolderTest {
	@Test
	public void testIndexedGmailFolder() {
		final FolderMock nonIndexedFolder = new FolderMock();
		FolderIndexMock folderIndex = new FolderIndexMock();
		folderIndex.addSignatureForId("#42$foo", "previous-signature");
		
		NonIndexGmailFolderProvider nonIndexedFolderMock = new NonIndexGmailFolderProvider() {
			@Override
			public GmailFolder get() {
				return nonIndexedFolder;
			}
		}; 
		IndexedGmailFolder indexedGmailFolder = new IndexedGmailFolder(nonIndexedFolderMock, folderIndex);
				
		SkypeChat previousSkypeChat = SkypeApiMock.produceChatMock("#42$foo", "zoe", "joe");
		GmailMessage gmailMessage = new GmailMessageMock(previousSkypeChat);
		indexedGmailFolder.appendMessage(gmailMessage);
		
		SkypeChat otherPreviousSkypeChat = SkypeApiMock.produceChatMock("#is-not-indexed", "not", "indexed");
		GmailMessage otherMessage = new GmailMessageMock(otherPreviousSkypeChat);
		indexedGmailFolder.appendMessage(otherMessage);
		
		SkypeChat skypeChat = SkypeApiMock.produceChatMock("#42$foo", "zoe", "joe");
		GmailMessage retrievedEntry = indexedGmailFolder.retrieveMessageEntryFor(skypeChat);
		Assert.assertTrue(Proxy.isProxyClass(retrievedEntry.getClass()));
		
		SkypeChat nonIndexedSkypeChat = SkypeApiMock.produceChatMock("#is-not-indexed", "not", "indexed");
		GmailMessage entryForNonIndexedChat = indexedGmailFolder.retrieveMessageEntryFor(nonIndexedSkypeChat);
		
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
