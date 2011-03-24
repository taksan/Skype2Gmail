package skype2disk;

import skype.SkypeApi;
import skype.SkypeApiImpl;
import skype.SkypeUserFactory;
import skype.SkypeUserFactoryImpl;
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
		bind(SkypeApi.class).to(SkypeApiImpl.class);
		bind(SkypeUserFactory.class).to(SkypeUserFactoryImpl.class);
		bind(LoggerProvider.class).to(LoggerProviderImpl.class);
	}
}
