package skype2disk;

import skype.Skype2StorageModuleCommons;
import skype.SkypeHistoryCliOptions;
import skype.SkypeStorage;

import com.google.inject.Scopes;

public class Skype2DiskModuleCommons extends Skype2StorageModuleCommons {
	
	private final String[] args;

	public Skype2DiskModuleCommons(String[] args)
	{
		this.args = args;
	}

	@Override
	protected void configure() {
		super.configure();
		bind(SkypeStorage.class).to(FileSystemStorage.class).in(Scopes.SINGLETON);
		bind(HistoryDir.class).to(SkypeHistoryCliOptions.class);
		bind(FileDumpContentParser.class).to(FileDumpContentParserImpl.class);
		bind(String[].class).toInstance(args);
	}
}
