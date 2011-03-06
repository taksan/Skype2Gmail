package skype;

import junit.framework.Assert;

import org.junit.Test;

import skype.mocks.ChatEntryBuilderFactoryMock;
import skype.mocks.SkypeApiMock;
import skype.mocks.SkypeStorageMock;
import skype.mocks.SkyperRecorderMockModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class SkypeRecorderTest {
	
	@Test
	public void happyDay()
	{
		SkypeApiMock skypeApi = new SkypeApiMock();
		skypeApi.addMockChat(SkypeApiMock.produceChatMock("42","moe","joe"));
		skypeApi.addMockChat(SkypeApiMock.produceChatMock("73","john","doe"));
		
		SkypeStorage skypeStorage = new SkypeStorageMock();
		ChatEntryBuilderFactory chatEntryBuilderFactory = new ChatEntryBuilderFactoryMock();
		
		SkypeRecorder skypeRecorder_SUBJECT = new SkypeRecorder(skypeApi, skypeStorage, chatEntryBuilderFactory);
		skypeRecorder_SUBJECT.record();
		
		String actual = skypeStorage.toString().trim();
		
		String expected = 
				"@SkypeStorageMock:\n" + 
				"@StorageEntryMock: ------\n" + 
				"chatId:42, topic:FOO, date:2011/04/21 15:00:00\n" + 
				"members: moe,joe\n" + 
				"[15:14:18] MOE: Hya\n" + 
				"[15:14:24] JOE: Howdy\n" + 
				"@StorageEntryMock: ------\n" + 
				"chatId:73, topic:FOO, date:2011/04/21 15:00:00\n" + 
				"members: john,doe\n" + 
				"[15:14:18] JOHN: Hya\n" + 
				"[15:14:24] DOE: Howdy";
		Assert.assertEquals(expected , actual);
	}

	@Test
	public void happyDayWithGuice()
	{
		Injector injector = Guice.createInjector(new SkyperRecorderMockModule());

		SkypeRecorder skypeRecorder_SUBJECT = (SkypeRecorder) injector.getInstance(SkypeHistoryRecorder.class);
		skypeRecorder_SUBJECT.record();
		
		SkypeStorage skypeStorage = injector.getInstance(SkypeStorage.class);
		String actual = skypeStorage.toString().trim();
		
		String expected = 
				"@SkypeStorageMock:\n" + 
				"@StorageEntryMock: ------\n" + 
				"chatId:42, topic:FOO, date:2011/04/21 15:00:00\n" + 
				"members: moe,joe\n" + 
				"[15:14:18] MOE: Hya\n" + 
				"[15:14:24] JOE: Howdy\n" + 
				"@StorageEntryMock: ------\n" + 
				"chatId:73, topic:FOO, date:2011/04/21 15:00:00\n" + 
				"members: john,doe\n" + 
				"[15:14:18] JOHN: Hya\n" + 
				"[15:14:24] DOE: Howdy";
		Assert.assertEquals(expected , actual);
	}
}
