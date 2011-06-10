package skype2gmail.mocks;

import mail.SkypeMailFolder;
import mail.mocks.FolderMock;
import skype.commons.SkypeStorage;
import skype.mocks.SkypeModuleMock;
import skype2gmail.MailStorage;
import skype2gmail.Skype2GmailModule;
import skype2gmail.UserCredentialsProvider;

import com.google.inject.Scopes;


public class Skype2GmailModuleMockingSkypeApi extends Skype2GmailModule {

	@Override
	public void configure() {
		super.configure();
		SkypeModuleMock.bindMocks(binder());
		
		bind(UserCredentialsProvider.class).to(CredentialsProviderMock.class).in(Scopes.SINGLETON);
		bind(SkypeMailFolder.class).to(FolderMock.class).in(Scopes.SINGLETON);
	}
}
