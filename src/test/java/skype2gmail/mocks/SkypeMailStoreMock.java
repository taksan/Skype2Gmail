package skype2gmail.mocks;

import javax.mail.Folder;
import javax.mail.Store;

import mail.SkypeMailStore;


public class SkypeMailStoreMock implements SkypeMailStore {

	final public JavaMailFolderMock javaMailFolderMock;
	final private Store mockStore;
	public boolean closeWasInvoked =false;
	
	public SkypeMailStoreMock() {
		mockStore = new JavaMailStoreMock();
		javaMailFolderMock = new JavaMailFolderMock(mockStore);
	}
	
	@Override
	public Folder getFolder(String folder) {
		return javaMailFolderMock;
	}

	@Override
	public void close() {
		closeWasInvoked  = true;
	}
}
