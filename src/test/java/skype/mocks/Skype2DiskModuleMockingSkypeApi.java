package skype.mocks;

import skype.BasePath;
import skype.LastSynchronizationProvider;
import skype.SkypeApi;
import skype.SkypeUserFactory;
import skype2disk.Skype2DiskModuleCommons;
import skype2disk.mocks.BasePathMock;
import utils.LoggerProvider;
import utils.SimpleLoggerProvider;

import com.google.inject.Scopes;

public class Skype2DiskModuleMockingSkypeApi extends Skype2DiskModuleCommons {

	public Skype2DiskModuleMockingSkypeApi(String [] args) {
		super(args);
	}
	
	@Override
	public void configure() {
		super.configure();
		bind(SkypeApi.class).to(SkypeApiImplMock.class).in(Scopes.SINGLETON);
		bind(SkypeUserFactory.class).to(SkypeUserFactoryMock.class).in(Scopes.SINGLETON);;
		bind(LoggerProvider.class).to(SimpleLoggerProvider.class).in(Scopes.SINGLETON);;
		bind(LastSynchronizationProvider.class).to(LastSynchronizationProviderMock.class).in(Scopes.SINGLETON);
		bind(BasePath.class).to(BasePathMock.class).in(Scopes.SINGLETON);
	}

}
