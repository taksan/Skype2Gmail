package skype.mocks;

import skype.ChatEntryBuilderFactory;
import skype.SkypeApi;
import skype.SkypeHistoryRecorder;
import skype.SkypeRecorder;
import skype.SkypeStorage;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;


public class SkyperRecorderMockModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(SkypeHistoryRecorder.class).to(SkypeRecorder.class);
		bind(SkypeStorage.class).toInstance(new SkypeStorageMock());
		bind(ChatEntryBuilderFactory.class).to(ChatEntryBuilderFactoryMock.class);
	}
	
	@Provides
	SkypeApi provideSkypeApi() {
		SkypeApiMock skypeApi = new SkypeApiMock();
		skypeApi.addMockChat(SkypeApiMock.produceChatMock("42","moe","joe"));
		skypeApi.addMockChat(SkypeApiMock.produceChatMock("73","john","doe"));
		
		return skypeApi;
	}

}
