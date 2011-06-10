package skype2disk;

import skype.commons.SkypeStorage;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;


public class Skype2DiskModule extends AbstractModule {
	private final String[] args;
	
	public Skype2DiskModule(String[] args)
	{
		this.args = args;
	}

	@Override
	protected void configure() {
		bind(SkypeStorage.class).to(FileSystemStorage.class).in(Scopes.SINGLETON);
		bind(String[].class).toInstance(args);
	}
}
