package skype2gmail;

import org.junit.Test;

import junit.framework.Assert;
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
		
		FolderIndexImpl subject = new FolderIndexImpl(folderMock);
		String actual = subject.getSignatureFor("#43$foo");
		
		Assert.assertEquals(skypeChat2.getBodySignature(), actual);

		SkypeChat skypeChat3 = SkypeApiMock.produceChatMock("#44$foo", "zoe", "joe","body-sig3");
		subject.addIndexFor(skypeChat3);
		subject.save();
	
		String updatedIndex = folderMock.retrieveIndexFromMail();
		
		final String expectedIndex = mockIndex+"\n"+makeEntry(skypeChat3);
		
		Assert.assertEquals(expectedIndex, updatedIndex);
		
	}

	private String makeEntry(SkypeChat skypeChat1) {
		return String.format("%s,%s",skypeChat1.getId(), skypeChat1.getBodySignature());
	}
}
