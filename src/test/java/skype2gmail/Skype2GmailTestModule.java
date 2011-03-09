package skype2gmail;

import skype.ChatContentBuilderFactory;
import skype.SkypeHistoryRecorder;
import skype.SkypeRecorder;
import skype.SkypeStorage;
import skype.mocks.SkyperRecorderMockModule;

public class Skype2GmailTestModule extends SkyperRecorderMockModule {
	@Override
	protected void configure() {
		bind(SkypeHistoryRecorder.class).to(SkypeRecorder.class);
		bind(SkypeStorage.class).toInstance(new GmailStorage());
		bind(ChatContentBuilderFactory.class).to(GmailContentBuilderFactory.class);
	}

}
