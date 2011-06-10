package skype.mocks;

import skype2disk.Skype2DiskModule;

public class Skype2DiskModuleMockingSkypeApi extends Skype2DiskModule {

	public Skype2DiskModuleMockingSkypeApi(String [] args) {
		super(args);
	}
	
	@Override
	public void configure() {
		super.configure();
		SkypeModuleMock.bindMocks(binder());
	}
}
