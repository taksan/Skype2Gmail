package skype2gmail;

import gmail.GmailFolder;
import gmail.GmailMessage;

import com.google.inject.Inject;

public class GmailMessageProviderImpl implements GmailMessageProvider {

	private RootFolderProvider rootFolderProvider;
	
	@Inject
	public GmailMessageProviderImpl(RootFolderProvider rootFolderProvider) {
		this.rootFolderProvider = rootFolderProvider;
		
	}

	@Override
	public GmailMessage[] getMessages() {
		GmailFolder root = this.rootFolderProvider.getInstance();
		return root.getMessages();
	}
}
