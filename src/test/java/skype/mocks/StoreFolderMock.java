package skype.mocks;

import gmail.GmailFolder;
import skype2gmail.GmailStoreFolder;

public class StoreFolderMock implements GmailStoreFolder {

	private GmailFolderMock gmailFolderMock;
	
	public StoreFolderMock() {
		gmailFolderMock = new GmailFolderMock();
	}

	@Override
	public GmailFolder getFolder() {
		return this.gmailFolderMock;
	}

	@Override
	public void close() {
		// nothing to do here
	}
}
