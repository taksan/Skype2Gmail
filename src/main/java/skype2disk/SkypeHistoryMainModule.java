package skype2disk;

import skype.BasePath;
import skype.UserHomeBasePath;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class SkypeHistoryMainModule extends AbstractModule {

	private final String[] args;

	public SkypeHistoryMainModule(String[] args) {
		this.args = args;
		
	}

	@Override
	protected void configure() {
		bind(String[].class).toInstance(args);
		bind(BasePath.class).to(UserHomeBasePath.class).in(Scopes.SINGLETON);
	}
}
