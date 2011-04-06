package skype2gmail;

import com.google.inject.Scopes;

import gmail.UserHomeConfigProvider;
import skype.SkypeApi;
import skype.SkypeApiImpl;
import skype.SkypeUserFactory;
import skype.SkypeUserFactoryImpl;
import utils.LoggerProviderImpl;
import utils.LoggerProvider;

public class Skype2GmailModule extends Skype2GmailModuleCommons {
	@Override
	protected void configure() {
		super.configure();
		bind(SkypeApi.class).to(SkypeApiImpl.class).in(Scopes.SINGLETON);
		bind(UserAuthProvider.class).to(UserHomeConfigProvider.class).in(Scopes.SINGLETON);
		bind(SkypeUserFactory.class).to(SkypeUserFactoryImpl.class).in(Scopes.SINGLETON);
		bind(GmailFolderStore.class).to(GmailFolderStoreImpl.class).in(Scopes.SINGLETON);
		bind(GmailMessageProvider.class).to(GmailMessageProviderImpl.class).in(Scopes.SINGLETON);
		bind(LoggerProvider.class).to(LoggerProviderImpl.class).in(Scopes.SINGLETON);
	}
}
