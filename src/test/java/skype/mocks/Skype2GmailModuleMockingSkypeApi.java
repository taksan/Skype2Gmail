package skype.mocks;

import skype.SkypeApi;
import skype.SkypeUserFactory;
import skype2gmail.GmailMessageProvider;
import skype2gmail.GmailFolderStore;
import skype2gmail.Skype2GmailModuleCommons;
import skype2gmail.UserAuthProvider;
import skype2gmail.mocks.MockAuthProvider;

import com.google.inject.Scopes;


public class Skype2GmailModuleMockingSkypeApi extends Skype2GmailModuleCommons {

	@Override
	public void configure() {
		super.configure();
		bind(SkypeApi.class).to(SkypeApiImplMock.class).in(Scopes.SINGLETON);
		bind(GmailMessageProvider.class).to(GmailMessageProviderMock.class).in(Scopes.SINGLETON);
		bind(GmailFolderStore.class).to(RootFolderProviderMock.class).in(Scopes.SINGLETON);
		
		bind(UserAuthProvider.class).to(MockAuthProvider.class).in(Scopes.SINGLETON);
		bind(SkypeUserFactory.class).to(SkypeUserFactoryMock.class).in(Scopes.SINGLETON);
	}

}
