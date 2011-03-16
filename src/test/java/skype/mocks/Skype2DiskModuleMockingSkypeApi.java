package skype.mocks;

import com.google.inject.Scopes;

import skype.SkypeApi;
import skype2disk.Skype2DiskModuleCommons;

public class Skype2DiskModuleMockingSkypeApi extends Skype2DiskModuleCommons {

	public Skype2DiskModuleMockingSkypeApi(String dumpTarget) {
		super(dumpTarget);
	}
	
	@Override
	public void configure() {
		super.configure();
		bind(SkypeApi.class).to(SkypeApiImplMock.class).in(Scopes.SINGLETON);
	}

}
