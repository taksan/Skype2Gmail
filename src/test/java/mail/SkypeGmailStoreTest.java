package mail;

import javax.mail.Folder;
import javax.mail.MessagingException;

import mail.mocks.MailStoreMock;
import mail.mocks.SessionProviderMock;

import org.junit.Assert;
import org.junit.Test;

import skype2gmail.UserCredentialsProvider;
import skype2gmail.mocks.JavaMailFolderMock;

public class SkypeGmailStoreTest {
	@Test
	public void testGetExistingFolder() throws MessagingException {
		boolean folderExists = true;
		SessionProviderMock sessionProvider = getSessionProviderAndFolderExistsAs(folderExists);
		
		UserCredentialsProvider userInfoProvider = getUserProviderMock();
		
		SkypeGmailStore skypeGmailStore = new SkypeGmailStore(sessionProvider, userInfoProvider);
		Folder folder = skypeGmailStore.getFolder("Skype-Chats-Foo");
		
		Assert.assertTrue("The folder should be open", folder.isOpen());
	}
	@Test
	public void testGetNonExistingFolder() throws MessagingException {
		boolean folderExists = true;
		SessionProviderMock sessionProvider = getSessionProviderAndFolderExistsAs(folderExists);
		
		UserCredentialsProvider userInfoProvider = getUserProviderMock();
		
		SkypeGmailStore skypeGmailStore = new SkypeGmailStore(sessionProvider, userInfoProvider);
		Folder folder = skypeGmailStore.getFolder("Skype-Chats-Foo");
		
		
		Assert.assertTrue("The folder should've been created", folder.exists());
		Assert.assertTrue("The folder should be open", folder.isOpen());
	}

	private SessionProviderMock getSessionProviderAndFolderExistsAs(
			boolean folderExists) {
		SessionProviderMock sessionProvider = new SessionProviderMock();
		MailStoreMock storeMock = sessionProvider.getMockStore();
		JavaMailFolderMock mailFolderMock = (JavaMailFolderMock) storeMock.getFolder("Skype-Chats-Foo");
		mailFolderMock.setExists(folderExists);
		return sessionProvider;
	}	

	private UserCredentialsProvider getUserProviderMock() {
		return new UserCredentialsProvider() {
			public String getUser() {
				return "foo";
			}
			public String getPassword() {
				return "bar";
			}
		};
	}
}
