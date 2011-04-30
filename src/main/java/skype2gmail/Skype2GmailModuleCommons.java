package skype2gmail;

import gmail.Skype2GmailConfigContents;
import gmail.Skype2GmailConfigContentsImpl;

import com.google.inject.Scopes;

import skype.Skype2StorageModuleCommons;
import skype.SkypeStorage;

public class Skype2GmailModuleCommons extends Skype2StorageModuleCommons {

	@Override
	protected void configure() {
		super.configure();
		bind(SkypeStorage.class).to(GmailStorage.class).in(Scopes.SINGLETON);
		bind(SkypeChatFolderProvider.class).to(DefaultSkypeChatFolderProvider.class).in(Scopes.SINGLETON);
		bind(SessionProvider.class).to(SessionProviderImpl.class).in(Scopes.SINGLETON);
		bind(GmailStorageEntryFactory.class).to(GmailStorageEntryFactoryImpl.class).in(Scopes.SINGLETON);
		bind(Skype2GmailConfigContents.class).to(Skype2GmailConfigContentsImpl.class).in(Scopes.SINGLETON);
		bind(GmailMessageFactory.class).to(GmailMessageFactoryImpl.class).in(Scopes.SINGLETON);
	}
}
