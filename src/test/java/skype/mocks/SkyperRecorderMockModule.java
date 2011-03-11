package skype.mocks;

import skype.ChatContentBuilderFactory;
import skype.SkypeApi;
import skype.SkypeHistoryRecorder;
import skype.SkypeRecorder;
import skype.SkypeStorage;
import skype2disk.FileDumpContentBuilderFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;


public class SkyperRecorderMockModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(SkypeHistoryRecorder.class).to(SkypeRecorder.class);
		bind(SkypeStorage.class).to(SkypeStorageMock.class).in(Scopes.SINGLETON);
		bind(ChatContentBuilderFactory.class).to(FileDumpContentBuilderFactory.class);
	}
	
	@Provides
	SkypeApi provideSkypeApi() {
		SkypeApiMock skypeApi = new SkypeApiMock();
		skypeApi.addMockChat(SkypeApiMock.produceChatMock("42","moe","joe"));
		skypeApi.addMockChat(SkypeApiMock.produceChatMock("73","john","doe"));
		
		return skypeApi;
	}

}
