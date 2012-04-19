package skype2gmail;

import mail.SkypeMailFolder;
import mail.SkypeMailFolderImpl;
import mail.SkypeMailStore;
import mail.skypemail.SkypeMailMessageFactory;

import com.google.inject.Inject;

public class NonIndexGmailFolderProviderImpl implements
		NonIndexGmailFolderProvider {
	
	private SkypeMailFolderImpl gmailFolderImpl;

	@Inject
	public NonIndexGmailFolderProviderImpl(SkypeChatFolderProvider chatFolderProvider, SkypeMailStore gmailStore, 
			SkypeMailMessageFactory gmailMessageFactory) {
		gmailFolderImpl = new SkypeMailFolderImpl(chatFolderProvider, gmailStore, gmailMessageFactory);
	}

	@Override
	public SkypeMailFolder get() {
		return gmailFolderImpl;
	}
}
