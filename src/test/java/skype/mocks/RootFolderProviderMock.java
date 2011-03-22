package skype.mocks;

import gmail.GmailFolder;
import skype2gmail.GmailFolderStore;

public class RootFolderProviderMock implements GmailFolderStore {

	private GmailFolderMock gmailFolderMock;
	
	public RootFolderProviderMock() {
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
