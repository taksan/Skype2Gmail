package skype.mocks;

import skype.ChatContentBuilderFactory;
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
		bind(ChatContentBuilderFactory.class).to(ChatContentBuilderFactoryMock.class);
	}
	
	@Provides
	SkypeApi provideSkypeApi() {
		SkypeApiMock skypeApi = new SkypeApiMock();
		skypeApi.addMockChat(SkypeApiMock.produceChatMock("42","moe","joe"));
		skypeApi.addMockChat(SkypeApiMock.produceChatMock("73","john","doe"));
		
		return skypeApi;
	}

}
