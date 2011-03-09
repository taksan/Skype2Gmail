package skype2gmail;

import skype.ChatContentBuilderFactory;
import skype.SkypeApi;
import skype.SkypeApiImpl;
import skype.SkypeHistoryRecorder;
import skype.SkypeRecorder;
import skype.SkypeStorage;

import com.google.inject.AbstractModule;

public class Skype2GmailModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(SkypeApi.class).to(SkypeApiImpl.class);
		bind(SkypeHistoryRecorder.class).to(SkypeRecorder.class);
		bind(SkypeStorage.class).toInstance(new GmailStorage());
		bind(ChatContentBuilderFactory.class).to(GmailContentBuilderFactory.class);
	}


}
