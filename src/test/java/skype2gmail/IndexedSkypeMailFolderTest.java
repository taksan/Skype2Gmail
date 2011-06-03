package skype2gmail;

import java.lang.reflect.Proxy;

import junit.framework.Assert;
import mail.SkypeMailFolder;
import mail.SkypeMailMessage;
import mail.mocks.FolderMock;

import org.junit.Test;

import skype.commons.SkypeChat;
import skype.mocks.SkypeApiMock;
import skype2gmail.mocks.FolderIndexMock;
import skype2gmail.mocks.SkypeMailMessageMock;

public class IndexedSkypeMailFolderTest {
	@Test
	public void retrieveAnIndexedChat_ShouldReturnOnlyAProxyReference() {
		IndexedSkypeMailFolder indexedGmailFolder_SUBJECT = createIndexedSkypeMailFolder();
		
		SkypeChat someIndexedChat = SkypeApiMock.produceChatMock("#42$foo", "zoe", "joe");
		SkypeMailMessage retrievedEntry = indexedGmailFolder_SUBJECT.retrieveMessageEntryFor(someIndexedChat);
		Assert.assertTrue(Proxy.isProxyClass(retrievedEntry.getClass()));
		Assert.assertTrue(Proxy.getInvocationHandler(retrievedEntry) instanceof MailMessageIndexEntry);
	}

	@Test
	public void retrieveNonIndexedChat_ShouldRetrieveTheWholeMessage() {
		FolderIndexMock folderIndex = new FolderIndexMock();
		IndexedSkypeMailFolder indexedGmailFolder_SUBJECT = createIndexedSkypeMailFolder(folderIndex);
				
		SkypeChat nonIndexedSkypeChat = SkypeApiMock.produceChatMock("#is-not-indexed", "not", "indexed");
		SkypeMailMessage entryForNonIndexedChat = indexedGmailFolder_SUBJECT.retrieveMessageEntryFor(nonIndexedSkypeChat);
		Assert.assertFalse(Proxy.isProxyClass(entryForNonIndexedChat.getClass()));
	}
	
	@Test
	public void retrieveNonIndexedChat_ShouldAddNewIndexEntryAfterRetrievalAttempt() {
		FolderIndexMock folderIndex = new FolderIndexMock();
		IndexedSkypeMailFolder indexedGmailFolder_SUBJECT = createIndexedSkypeMailFolder(folderIndex);
				
		SkypeChat nonIndexedSkypeChat = SkypeApiMock.produceChatMock("#is-not-indexed", "not", "indexed");
		
		Assert.assertNull(folderIndex.getSignatureFor("#is-not-indexed"));
		
		indexedGmailFolder_SUBJECT.retrieveMessageEntryFor(nonIndexedSkypeChat);
		
		String sig = folderIndex.getSignatureFor("#is-not-indexed");
		Assert.assertEquals(nonIndexedSkypeChat.getBodySignature(), sig);
	}
	
	@Test
	public void retrieveSomeChat_ShouldSaveIndexAfterClose() {
		FolderIndexMock folderIndex = new FolderIndexMock();
		IndexedSkypeMailFolder indexedGmailFolder_SUBJECT = createIndexedSkypeMailFolder(folderIndex);
				
		SkypeChat nonIndexedSkypeChat = SkypeApiMock.produceChatMock("#is-not-indexed", "not", "indexed");
		indexedGmailFolder_SUBJECT.retrieveMessageEntryFor(nonIndexedSkypeChat);
		
		Assert.assertEquals(false, folderIndex.wasSaved());
		indexedGmailFolder_SUBJECT.close();
		Assert.assertEquals(true, folderIndex.wasSaved());
	}
	
	
	private IndexedSkypeMailFolder createIndexedSkypeMailFolder() {
		return createIndexedSkypeMailFolder(new FolderIndexMock());
	}

	private IndexedSkypeMailFolder createIndexedSkypeMailFolder(
			FolderIndexMock folderIndex) {
		final FolderMock nonIndexedFolder = new FolderMock();
		folderIndex.addSignatureForId("#42$foo", "previous-signature");
		
		NonIndexGmailFolderProvider nonIndexedFolderMock = new NonIndexGmailFolderProvider() {
			@Override
			public SkypeMailFolder get() {
				return nonIndexedFolder;
			}
		}; 
		IndexedSkypeMailFolder indexedGmailFolder = new IndexedSkypeMailFolder(nonIndexedFolderMock, folderIndex);
		addPreviousMessages(indexedGmailFolder);
		return indexedGmailFolder;
	}

	private void addPreviousMessages(IndexedSkypeMailFolder indexedGmailFolder) {
		SkypeChat previousSkypeChat = SkypeApiMock.produceChatMock("#42$foo", "zoe", "joe");
		SkypeMailMessage gmailMessage = new SkypeMailMessageMock(previousSkypeChat);
		indexedGmailFolder.appendMessage(gmailMessage);
		
		SkypeChat otherPreviousSkypeChat = SkypeApiMock.produceChatMock("#is-not-indexed", "not", "indexed");
		SkypeMailMessage otherMessage = new SkypeMailMessageMock(otherPreviousSkypeChat);
		indexedGmailFolder.appendMessage(otherMessage);
	}
}
