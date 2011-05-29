package skype2gmail;

import mail.SkypeMailMessageFactory;
import mail.SkypeMailMessageFactoryImpl;
import skype.commons.Skype2StorageModuleCommons;
import skype.commons.SkypeStorage;

import com.google.inject.Scopes;

public class Skype2GmailModuleCommons extends Skype2StorageModuleCommons {

	@Override
	protected void configure() {
		super.configure();
		bind(SkypeStorage.class).to(MailStorage.class).in(Scopes.SINGLETON);
		bind(SkypeChatFolderProvider.class).to(DefaultSkypeChatFolderProvider.class).in(Scopes.SINGLETON);
		bind(SessionProvider.class).to(SessionProviderImpl.class).in(Scopes.SINGLETON);
		bind(MailStorageEntryFactory.class).to(MailStorageEntryFactoryImpl.class).in(Scopes.SINGLETON);
		bind(SkypeMailMessageFactory.class).to(SkypeMailMessageFactoryImpl.class).in(Scopes.SINGLETON);
	}
}
