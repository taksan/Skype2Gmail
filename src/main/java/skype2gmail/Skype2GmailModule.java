package skype2gmail;

import skype.commons.SkypeStorage;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class Skype2GmailModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(SkypeStorage.class).to(MailStorage.class).in(Scopes.SINGLETON);
		bind(NonIndexGmailFolderProvider.class).to(NonIndexGmailFolderProviderImpl.class).in(Scopes.SINGLETON);
		bind(FolderIndex.class).to(FolderIndexImpl.class).in(Scopes.SINGLETON);
	}
}
