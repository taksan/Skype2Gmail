package mail.mocks;

import javax.mail.Folder;

import mail.MailStore;

import org.apache.commons.lang.NotImplementedException;

import skype2gmail.mocks.JavaMailFolderMock;
import skype2gmail.mocks.JavaMailStoreMock;

public class MailStoreMock implements MailStore {
	
	private JavaMailFolderMock mailFolderMock;

	public MailStoreMock() {
		JavaMailStoreMock storeMock = new JavaMailStoreMock();
		mailFolderMock = new JavaMailFolderMock(storeMock);
	}
	
	@Override
	public Folder getFolder(String folderName) {
		return mailFolderMock;
	}

	@Override
	public void connect(String address, String user, String password) {
		// nothing here
	}
	
	@Override
	public void close() {
		throw new NotImplementedException();
	}
}
