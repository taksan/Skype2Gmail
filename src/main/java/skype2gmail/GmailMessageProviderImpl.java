package skype2gmail;

import gmail.GmailFolder;

import javax.mail.Message;

import com.google.inject.Inject;

public class GmailMessageProviderImpl implements GmailMessageProvider {

	private RootFolderProvider rootFolderProvider;
	
	@Inject
	public GmailMessageProviderImpl(RootFolderProvider rootFolderProvider) {
		this.rootFolderProvider = rootFolderProvider;
		
	}

	@Override
	public Message[] getMessages() {
		GmailFolder root = this.rootFolderProvider.getInstance();
		return root.getMessages();
	}
}
