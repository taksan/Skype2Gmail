package skype;

import junit.framework.Assert;

import org.junit.Test;

import skype.mocks.SkypeApiMock;
import skype.mocks.SkypeStorageMock;

public class SkypeRecorderTest {
	
	@Test
	public void simpleRecorderTest()
	{
		SkypeApiMock skypeApi = new SkypeApiMock();
		skypeApi.addMockChat(SkypeApiMock.produceChatMock("42","moe","joe"));
		skypeApi.addMockChat(SkypeApiMock.produceChatMock("73","john","doe"));
		
		SkypeStorage skypeStorage = new SkypeStorageMock();
		
		SkypeRecorder skypeRecorder_SUBJECT = new SkypeRecorder(skypeApi, skypeStorage);
		skypeRecorder_SUBJECT.record();
		
		final String actualForFirstRecord = skypeStorage.toString().trim();
		
		String expected = 
				"@SkypeStorageMock:\n" + 
				"@StorageEntryMock: ------\n" + 
				"chatAuthor:\n" + 
				"chatId:42\n" + 
				"Time:2011/04/21 15:00:00\n" + 
				"Body Signature:content-id-mock\n" + 
				"Messages Signatures:17f4007f9024da870afae8e60f6635fd,d6bbf5c7f50d1a96fcc3a2156dbf2b86\n" + 
				"Topic:FOO\n" + 
				"Poster:id=joe; displayName=JOE\n" + 
				"Poster:id=moe; displayName=MOE\n" + 
				"[2011/04/21 15:14:18] MOE: Hya\n" + 
				"[2011/04/21 15:14:24] JOE: Howdy\n" + 
				"	I'm doing fine\n" + 
				"Last modified:2011/04/21 15:14:24\n" + 
				"@StorageEntryMock: ------\n" + 
				"chatAuthor:\n" + 
				"chatId:73\n" + 
				"Time:2011/04/21 15:00:00\n" + 
				"Body Signature:content-id-mock\n" + 
				"Messages Signatures:c0687481b3f39aac8fd1ad874f604301,38e57729cade7223217d12bee978a509\n" + 
				"Topic:FOO\n" + 
				"Poster:id=doe; displayName=DOE\n" + 
				"Poster:id=john; displayName=JOHN\n" + 
				"[2011/04/21 15:14:18] JOHN: Hya\n" + 
				"[2011/04/21 15:14:24] DOE: Howdy\n" + 
				"	I'm doing fine\n" + 
				"Last modified:2011/04/21 15:14:24";
		Assert.assertEquals(expected , actualForFirstRecord);
		
		skypeRecorder_SUBJECT.record();
		
		final String actualForSecondRecord = skypeStorage.toString().trim();
		
		Assert.assertEquals(expected , actualForSecondRecord);
		
		skypeRecorder_SUBJECT.record();
	}
}
