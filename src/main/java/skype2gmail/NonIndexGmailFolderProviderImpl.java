package skype2gmail;

import com.google.inject.Inject;

public class NonIndexGmailFolderProviderImpl implements
		NonIndexGmailFolderProvider {
	
	private GmailFolderImpl gmailFolderImpl;

	@Inject
	public NonIndexGmailFolderProviderImpl(SkypeChatFolderProvider chatFolderProvider, GmailStore gmailStore) {
		gmailFolderImpl = new GmailFolderImpl(chatFolderProvider, gmailStore);
	}

	@Override
	public GmailFolder get() {
		return gmailFolderImpl;
	}
}
