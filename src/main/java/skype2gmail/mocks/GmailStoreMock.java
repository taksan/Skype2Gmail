package skype2gmail.mocks;

import javax.mail.Folder;
import javax.mail.Store;

import org.apache.commons.lang.NotImplementedException;

import skype2gmail.GmailStore;

public class GmailStoreMock implements GmailStore {

	private Store mockStore;
	
	public GmailStoreMock() {
		mockStore = new JavaMailStoreMock();
	}
	
	@Override
	public Folder getFolder(String folder) {
		return new JavaMailFolderMock(mockStore);
	}

	@Override
	public void close() {
		throw new NotImplementedException();
	}

}
