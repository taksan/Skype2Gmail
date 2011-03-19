package skype.mocks;

import javax.mail.Message;

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
	public Message[] getMessages() {
		return rootFolderProvider.getInstance().getMessages();
	}

}
