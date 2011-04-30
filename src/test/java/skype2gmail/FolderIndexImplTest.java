package skype2gmail;

import gmail.GmailMessage;
import junit.framework.Assert;

import org.junit.Test;

import skype.SkypeChat;
import skype.mocks.FolderMock;
import skype.mocks.SkypeApiMock;

public class FolderIndexImplTest {
	@Test
	public void testFolderIndexImpl() {
		
		SkypeChat skypeChat1 = SkypeApiMock.produceChatMock("#42$foo", "zoe", "joe","body-sig1");
		SkypeChat skypeChat2 = SkypeApiMock.produceChatMock("#43$foo", "zoe", "joe","body-sig2");
		String mockIndex = 
			makeEntry(skypeChat1)+"\n"+
			makeEntry(skypeChat2);
		
		
		FolderMock folderMock = new FolderMock(mockIndex);
		GmailMessageMock gmailMessageMock = new GmailMessageMock(skypeChat1);
		folderMock.appendMessage(gmailMessageMock);
		
		FolderIndexImpl subject = new FolderIndexImpl(folderMock, new GmailMessageFactoryMock());
		String actual = subject.getSignatureFor("#43$foo");
		
		Assert.assertEquals(skypeChat2.getBodySignature(), actual);

		SkypeChat skypeChat3 = SkypeApiMock.produceChatMock("#44$foo", "zoe", "joe","body-sig3");
		subject.addIndexFor(skypeChat3);
		subject.save();
		
		GmailMessage indexMessage = folderMock.retrieveFirstMessageMatchingSearchTerm(FolderIndex.CHAT_INDEX_SEARCH_TERM);
		String updatedIndex = indexMessage.getBody();
		
		final String expectedIndex = mockIndex+"\n"+makeEntry(skypeChat3);
		
		Assert.assertEquals(expectedIndex, updatedIndex);
	}

	private String makeEntry(SkypeChat skypeChat) {
		return String.format("%s,%s",skypeChat.getId(), skypeChat.getBodySignature());
	}
}
