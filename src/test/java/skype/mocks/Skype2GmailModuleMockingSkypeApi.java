package skype.mocks;

import skype.SkypeApi;
import skype.SkypeUserFactory;
import skype2gmail.GmailStore;
import skype2gmail.Skype2GmailModuleCommons;
import skype2gmail.UserAuthProvider;
import skype2gmail.mocks.MockAuthProvider;
import utils.LoggerProvider;
import utils.SimpleLoggerProvider;

import com.google.inject.Scopes;


public class Skype2GmailModuleMockingSkypeApi extends Skype2GmailModuleCommons {

	@Override
	public void configure() {
		super.configure();
		bind(SkypeApi.class).to(SkypeApiImplMock.class).in(Scopes.SINGLETON);
		bind(GmailStore.class).to(StoreMock.class).in(Scopes.SINGLETON);
		
		bind(UserAuthProvider.class).to(MockAuthProvider.class).in(Scopes.SINGLETON);
		bind(SkypeUserFactory.class).to(SkypeUserFactoryMock.class).in(Scopes.SINGLETON);
		bind(LoggerProvider.class).to(SimpleLoggerProvider.class);
		
	}

}
