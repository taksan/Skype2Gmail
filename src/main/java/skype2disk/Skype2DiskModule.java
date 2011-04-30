package skype2disk;

import com.google.inject.Scopes;

import skype.SkypeApi;
import skype.SkypeApiImpl;
import skype.SkypeUserFactory;
import skype.SkypeUserFactoryImpl;
import skype2gmail.Skype2GmailConfigContents;
import skype2gmail.Skype2GmailConfigContentsImpl;
import utils.LoggerProvider;
import utils.LoggerProviderImpl;

public class Skype2DiskModule extends Skype2DiskModuleCommons {
	
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
	}
}
