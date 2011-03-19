package skype2gmail;

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
	}
}
