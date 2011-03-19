package skype.mocks;

import gmail.GmailMessage;
import skype2gmail.GmailMessageProvider;
import skype2gmail.RootFolderProvider;

import com.google.inject.Inject;

public class GmailMessageProviderMock implements GmailMessageProvider {
	
	private final RootFolderProvider rootFolderProvider;

	@Inject
	public GmailMessageProviderMock(RootFolderProvider rootFolderProvider) {
		this.rootFolderProvider = rootFolderProvider;
		
	}

	@Override
	public GmailMessage[] getMessages() {
		return rootFolderProvider.getInstance().getMessages();
	}

}
