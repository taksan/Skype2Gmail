package skype2disk;

import skype.SkypeApi;
import skype.SkypeApiImpl;
import skype.SkypeUserFactory;
import skype.SkypeUserFactoryImpl;

public class Skype2DiskModule extends Skype2DiskModuleCommons {
	
	public Skype2DiskModule(String dumpTarget)
	{
		super(dumpTarget);
	}

	@Override
	protected void configure() {
		super.configure();
		bind(SkypeApi.class).to(SkypeApiImpl.class);
		bind(SkypeUserFactory.class).to(SkypeUserFactoryImpl.class);
	}

}
