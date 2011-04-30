package skype2gmail.mocks;

import javax.mail.Folder;
import javax.mail.Store;

import mail.SkypeMailStore;

import org.apache.commons.lang.NotImplementedException;


public class SkypeMailStoreMock implements SkypeMailStore {

	private Store mockStore;
	
	public SkypeMailStoreMock() {
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
