package skype2gmail;

import mail.SkypeMailFolder;
import mail.UserConfigBasedProvider;
import skype.BasePath;
import skype.LastSynchronizationProvider;
import skype.LastSynchronizationProviderImpl;
import skype.SkypeApi;
import skype.SkypeApiImpl;
import skype.SkypeUserFactory;
import skype.SkypeUserFactoryImpl;
import skype.UserHomeBasePath;
import utils.LoggerProvider;
import utils.LoggerProviderImpl;

import com.google.inject.Scopes;

public final class Skype2GmailModule extends Skype2GmailModuleCommons {
	@Override
	protected void configure() {
		super.configure();
		bind(SkypeApi.class).to(SkypeApiImpl.class).in(Scopes.SINGLETON);
		bind(UserAuthProvider.class).to(UserConfigBasedProvider.class).in(Scopes.SINGLETON);
		bind(SkypeUserFactory.class).to(SkypeUserFactoryImpl.class).in(Scopes.SINGLETON);
		bind(LoggerProvider.class).to(LoggerProviderImpl.class).in(Scopes.SINGLETON);
		
		bind(NonIndexGmailFolderProvider.class).to(NonIndexGmailFolderProviderImpl.class).in(Scopes.SINGLETON);
		bind(FolderIndex.class).to(FolderIndexImpl.class).in(Scopes.SINGLETON);
		bind(SkypeMailFolder.class).to(IndexedSkypeMailFolder.class).in(Scopes.SINGLETON);
		bind(LastSynchronizationProvider.class).to(LastSynchronizationProviderImpl.class).in(Scopes.SINGLETON);
		bind(BasePath.class).to(UserHomeBasePath.class).in(Scopes.SINGLETON);
	}
}
