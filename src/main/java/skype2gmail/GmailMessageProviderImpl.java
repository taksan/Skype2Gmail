package skype2gmail;

import gmail.GmailFolder;
import gmail.GmailMessage;

import com.google.inject.Inject;

public class GmailMessageProviderImpl implements GmailMessageProvider {

	private GmailFolderStore rootFolderProvider;
	
	@Inject
	public GmailMessageProviderImpl(GmailFolderStore rootFolderProvider) {
		this.rootFolderProvider = rootFolderProvider;
		
	}

	@Override
	public GmailMessage[] getMessages() {
		GmailFolder root = this.rootFolderProvider.getFolder();
		return root.getMessages();
	}
}
