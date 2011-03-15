package skype;

import junit.framework.Assert;

import org.junit.Test;

import skype.mocks.ChatContentBuilderFactoryMock;
import skype.mocks.SkypeApiMock;
import skype.mocks.SkypeStorageMock;

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
		
		final String actualForFirstRecord = skypeStorage.toString().trim();
		
		String expected = 
				"@SkypeStorageMock:\n" + 
				"@StorageEntryMock: ------\n" + 
				"chatId:42, topic:FOO, date:2011/04/21 15:00:00\n" + 
				"members: id=joe; displayName=JOE,id=moe; displayName=MOE\n" + 
				"[2011/04/21 15:14:18] MOE: Hya\n" + 
				"[2011/04/21 15:14:24] JOE: Howdy\n" + 
				"	I'm doing fine\n" + 
				"Last modified:2011/04/21 15:14:24\n" + 
				"@StorageEntryMock: ------\n" + 
				"chatId:73, topic:FOO, date:2011/04/21 15:00:00\n" + 
				"members: id=doe; displayName=DOE,id=john; displayName=JOHN\n" + 
				"[2011/04/21 15:14:18] JOHN: Hya\n" + 
				"[2011/04/21 15:14:24] DOE: Howdy\n" + 
				"	I'm doing fine\n" + 
				"Last modified:2011/04/21 15:14:24";
		Assert.assertEquals(expected , actualForFirstRecord);
		
		skypeRecorder_SUBJECT.record();
		
		final String actualForSencodRecord = skypeStorage.toString().trim();
		
		Assert.assertEquals(expected , actualForSencodRecord);
		
		skypeRecorder_SUBJECT.record();
	}
}
