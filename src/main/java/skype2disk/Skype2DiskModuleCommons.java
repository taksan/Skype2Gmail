package skype2disk;

import java.text.DateFormat;

import skype.SkypeChatFactory;
import skype.SkypeChatFactoryImpl;
import skype.SkypeChatMessage;
import skype.SkypeHistoryRecorder;
import skype.SkypeRecorder;
import skype.SkypeStorage;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;

public class Skype2DiskModuleCommons extends AbstractModule {
	
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
		bind(SkypeChatFactory.class).to(SkypeChatFactoryImpl.class);
		bind(SkypeHistoryRecorder.class).to(SkypeRecorder.class);
		bind(SkypeStorage.class).to(FileSystemStorage.class).in(Scopes.SINGLETON);
		bind(FileDumpContentParser.class).to(FileDumpContentParserImpl.class);
		bind(String.class).annotatedWith(Names.named("History Target")).toInstance(dumpTarget);
		bind(DateFormat.class).annotatedWith(Names.named("Skype Chat Date Format")).toInstance(SkypeChatMessage.chatDateFormat);
		bind(DateFormat.class).annotatedWith(Names.named("Skype Message Date Format")).toInstance(SkypeChatMessage.chatMessageDateFormat);
	}


	private String getDefaultDumpDirectory() {
		return System.getProperty("user.home")+"/.skypeChatHistory";
	}
}
