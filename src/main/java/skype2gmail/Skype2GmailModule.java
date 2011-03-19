package skype2gmail;

import com.google.inject.Scopes;

import gmail.UserHomeConfigProvider;
import skype.SkypeApi;
import skype.SkypeApiImpl;
import skype.SkypeUserFactory;
import skype.SkypeUserFactoryImpl;

public class Skype2GmailModule extends Skype2GmailModuleCommons {
	@Override
	protected void configure() {
		super.configure();
		bind(SkypeApi.class).to(SkypeApiImpl.class).in(Scopes.SINGLETON);
		bind(UserAuthProvider.class).to(UserHomeConfigProvider.class).in(Scopes.SINGLETON);
		bind(SkypeUserFactory.class).to(SkypeUserFactoryImpl.class).in(Scopes.SINGLETON);
		bind(RootFolderProvider.class).to(RootFolderProviderImpl.class).in(Scopes.SINGLETON);
		bind(GmailMessageProvider.class).to(GmailMessageProviderImpl.class).in(Scopes.SINGLETON);
	}
}
