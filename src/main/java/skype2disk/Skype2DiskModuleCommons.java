package skype2disk;

import skype.Skype2StorageModuleCommons;
import skype.SkypeStorage;

import com.google.inject.Scopes;
import com.google.inject.name.Names;

public class Skype2DiskModuleCommons extends Skype2StorageModuleCommons {
	
	private String dumpTarget;

	public Skype2DiskModuleCommons(String dumpTarget)
	{
		if (dumpTarget == null) {
			dumpTarget = getDefaultDumpDirectory();
		}
		this.dumpTarget = dumpTarget;
	}

	@Override
	protected void configure() {
		super.configure();
		bind(SkypeStorage.class).to(FileSystemStorage.class).in(Scopes.SINGLETON);
		bind(String.class).annotatedWith(Names.named("History Target")).toInstance(dumpTarget);
		bind(FileDumpContentParser.class).to(FileDumpContentParserImpl.class);
	}


	private String getDefaultDumpDirectory() {
		return System.getProperty("user.home")+"/.skypeChatHistory";
	}
}
