package skype;

import junit.framework.Assert;

import org.junit.Test;

import skype.mocks.ChatContentBuilderFactoryMock;
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
		
		SkypeStorage skypeStorage = new SkypeStorageMock(new ChatContentBuilderFactoryMock());
		
		SkypeRecorder skypeRecorder_SUBJECT = new SkypeRecorder(skypeApi, skypeStorage);
		skypeRecorder_SUBJECT.record();
		
		String actual = skypeStorage.toString().trim();
		
		String expected = 
				"@SkypeStorageMock:\n" + 
				"@StorageEntryMock: ------\n" + 
				"chatId:42, topic:FOO, date:2011/04/21 15:00:00\n" + 
				"members: moe,joe\n" + 
				"[15:14:18] MOE: Hya\n" + 
				"[15:14:24] JOE: Howdy\n" + 
				"Last modified:2011/04/21 15:14:24\n" + 
				"@StorageEntryMock: ------\n" + 
				"chatId:73, topic:FOO, date:2011/04/21 15:00:00\n" + 
				"members: john,doe\n" + 
				"[15:14:18] JOHN: Hya\n" + 
				"[15:14:24] DOE: Howdy\n" + 
				"Last modified:2011/04/21 15:14:24";
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
				"Chat Content Code: content-id-mock\n" + 
				"Chat topic: FOO\n" + 
				"Chat [42] at 2011/04/21 15:00:00\n" + 
				"Chat members: [moe,joe]\n" + 
				"Messages Ids: [17f4007f9024da870afae8e60f6635fd,49b2215b046c7df8f731b7a0f48416e1]\n" + 
				"[15:14:18] MOE: Hya\n" + 
				"[15:14:24] JOE: Howdy\n" + 
				"Last modified:2011/04/21 15:14:24\n" + 
				"@StorageEntryMock: ------\n" + 
				"chatId:73, topic:FOO, date:2011/04/21 15:00:00\n" + 
				"Chat Content Code: content-id-mock\n" + 
				"Chat topic: FOO\n" + 
				"Chat [73] at 2011/04/21 15:00:00\n" + 
				"Chat members: [john,doe]\n" + 
				"Messages Ids: [c0687481b3f39aac8fd1ad874f604301,ed3cab6f55e984fd9537e7ae008ad21c]\n" + 
				"[15:14:18] JOHN: Hya\n" + 
				"[15:14:24] DOE: Howdy\n" + 
				"Last modified:2011/04/21 15:14:24";
		Assert.assertEquals(expected , actual);
	}
}
