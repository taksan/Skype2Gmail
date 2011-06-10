package skype.mocks;

import skype.commons.BasePath;
import skype.commons.LastSynchronizationProvider;
import skype.commons.SkypeApi;
import skype.commons.SkypeUserFactory;
import skype2disk.mocks.BasePathMock;
import utils.LoggerProvider;
import utils.SimpleLoggerProvider;

import com.google.inject.Binder;
import com.google.inject.Scopes;

public class SkypeModuleMock {

	public static void bindMocks(Binder binder) {
		binder.bind(SkypeApi.class).to(SkypeApiImplMock.class).in(Scopes.SINGLETON);
		binder.bind(SkypeUserFactory.class).to(SkypeUserFactoryMock.class).in(Scopes.SINGLETON);;
		binder.bind(LoggerProvider.class).to(SimpleLoggerProvider.class).in(Scopes.SINGLETON);;
		binder.bind(LastSynchronizationProvider.class).to(LastSynchronizationProviderMock.class).in(Scopes.SINGLETON);
		binder.bind(BasePath.class).to(BasePathMock.class).in(Scopes.SINGLETON);
	}
}
