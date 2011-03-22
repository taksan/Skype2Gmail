package skype.mocks;

import gmail.GmailMessage;
import skype2gmail.GmailMessageProvider;
import skype2gmail.GmailFolderStore;

import com.google.inject.Inject;

public class GmailMessageProviderMock implements GmailMessageProvider {
	
	private final GmailFolderStore rootFolderProvider;

	@Inject
	public GmailMessageProviderMock(GmailFolderStore rootFolderProvider) {
		this.rootFolderProvider = rootFolderProvider;
		
	}

	@Override
	public GmailMessage[] getMessages() {
		return rootFolderProvider.getFolder().getMessages();
	}

}
