package skype2disk;

import skype.commons.BasePath;
import skype.commons.LastSynchronizationProvider;
import skype.commons.LastSynchronizationProviderImpl;
import skype.commons.SkypeApi;
import skype.commons.SkypeApiImpl;
import skype.commons.SkypeUserFactory;
import skype.commons.SkypeUserFactoryImpl;
import skype.commons.UserHomeBasePath;
import skype2gmail.Skype2GmailConfigContents;
import skype2gmail.Skype2GmailConfigContentsImpl;
import utils.LoggerProvider;
import utils.LoggerProviderImpl;

import com.google.inject.Scopes;

public final class Skype2DiskModule extends Skype2DiskModuleCommons {
	
	public Skype2DiskModule(String[] args)
	{
		super(args);
	}

	@Override
	protected void configure() {
		super.configure();
		bind(SkypeApi.class).to(SkypeApiImpl.class).in(Scopes.SINGLETON);
		bind(SkypeUserFactory.class).to(SkypeUserFactoryImpl.class).in(Scopes.SINGLETON);
		bind(Skype2GmailConfigContents.class).to(Skype2GmailConfigContentsImpl.class).in(Scopes.SINGLETON);
		bind(LoggerProvider.class).to(LoggerProviderImpl.class).in(Scopes.SINGLETON);
		bind(LastSynchronizationProvider.class).to(LastSynchronizationProviderImpl.class).in(Scopes.SINGLETON);
		bind(BasePath.class).to(UserHomeBasePath.class).in(Scopes.SINGLETON);
	}
}
