package skype2disk;

import skype.ChatEntryBuilderFactory;
import skype.SkypeApi;
import skype.SkypeApiImpl;
import skype.SkypeHistoryRecorder;
import skype.SkypeRecorder;
import skype.SkypeStorage;

import com.google.inject.AbstractModule;

public class Skype2DiskModule extends AbstractModule {
	
	private String dumpTarget;

	public Skype2DiskModule(String dumpTarget)
	{
		if (dumpTarget == null) {
			dumpTarget = getDefaultDumpDirectory();
		}
		this.dumpTarget = dumpTarget;
	}

	private String getDefaultDumpDirectory() {
		return System.getProperty("user.home")+"/.skypeChatHistory";
	}

	@Override
	protected void configure() {
		bind(SkypeApi.class).to(SkypeApiImpl.class);
		bind(SkypeHistoryRecorder.class).to(SkypeRecorder.class);
		bind(SkypeStorage.class).toInstance(new FileSystemStorage(this.dumpTarget));
		bind(ChatEntryBuilderFactory.class).to(FileDumpEntryBuilderFactory.class);
	}

}
