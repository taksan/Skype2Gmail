package skype2gmail;

import mail.SkypeMailFolder;
import mail.UserConfigBasedProvider;
import skype.commons.BasePath;
import skype.commons.LastSynchronizationProvider;
import skype.commons.LastSynchronizationProviderImpl;
import skype.commons.SkypeApi;
import skype.commons.SkypeApiImpl;
import skype.commons.SkypeUserFactory;
import skype.commons.SkypeUserFactoryImpl;
import skype.commons.UserHomeBasePath;
import utils.LoggerProvider;
import utils.LoggerProviderImpl;

import com.google.inject.Scopes;

public final class Skype2GmailModule extends Skype2GmailModuleCommons {
	@Override
	protected void configure() {
		super.configure();
		bind(SkypeApi.class).to(SkypeApiImpl.class).in(Scopes.SINGLETON);
		bind(UserCredentialsProvider.class).to(UserConfigBasedProvider.class).in(Scopes.SINGLETON);
		bind(SkypeUserFactory.class).to(SkypeUserFactoryImpl.class).in(Scopes.SINGLETON);
		bind(LoggerProvider.class).to(LoggerProviderImpl.class).in(Scopes.SINGLETON);
		
		bind(NonIndexGmailFolderProvider.class).to(NonIndexGmailFolderProviderImpl.class).in(Scopes.SINGLETON);
		bind(FolderIndex.class).to(FolderIndexImpl.class).in(Scopes.SINGLETON);
		bind(SkypeMailFolder.class).to(IndexedSkypeMailFolder.class).in(Scopes.SINGLETON);
		bind(LastSynchronizationProvider.class).to(LastSynchronizationProviderImpl.class).in(Scopes.SINGLETON);
		bind(BasePath.class).to(UserHomeBasePath.class).in(Scopes.SINGLETON);
	}
}
