package skype.mocks;

import gmail.GmailFolder;
import skype2gmail.RootFolderProvider;

public class RootFolderProviderMock implements RootFolderProvider {

	private GmailFolderMock gmailFolderMock;
	
	public RootFolderProviderMock() {
		gmailFolderMock = new GmailFolderMock();
	}

	@Override
	public GmailFolder getInstance() {
		return this.gmailFolderMock;
	}

}
