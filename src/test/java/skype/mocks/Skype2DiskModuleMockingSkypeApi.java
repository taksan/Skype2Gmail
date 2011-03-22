package skype.mocks;

import skype.SkypeApi;
import skype.SkypeUserFactory;
import skype2disk.Skype2DiskModuleCommons;

import com.google.inject.Scopes;

public class Skype2DiskModuleMockingSkypeApi extends Skype2DiskModuleCommons {

	public Skype2DiskModuleMockingSkypeApi(String [] args) {
		super(args);
	}
	
	@Override
	public void configure() {
		super.configure();
		bind(SkypeApi.class).to(SkypeApiImplMock.class).in(Scopes.SINGLETON);
		bind(SkypeUserFactory.class).to(SkypeUserFactoryMock.class);
	}

}
