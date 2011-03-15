package skype2disk;

import skype.SkypeApi;
import skype.SkypeApiImpl;

public class Skype2DiskModule extends Skype2DiskModuleCommons {
	
	public Skype2DiskModule(String dumpTarget)
	{
		super(dumpTarget);
	}

	@Override
	protected void configure() {
		super.configure();
		bind(SkypeApi.class).to(SkypeApiImpl.class);
	}

}
