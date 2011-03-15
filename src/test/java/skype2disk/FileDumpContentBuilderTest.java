package skype2disk;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

import skype.ChatContentBuilderFactory;
import skype.SkypeRecorder;
import skype.SkypeStorage;
import skype.mocks.SkypeApiMock;
import skype.mocks.SkypeChatMock;
import skype.mocks.SkypeStorageMock;

public class FileDumpContentBuilderTest {

	@Test
	public void happyDayTest() throws ParseException {
		SkypeChatMock chat = SkypeApiMock.produceChatMock("#42;$foo","moe","joe");
		FileDumpContentBuilder fileDumpEntryBuilder = new FileDumpContentBuilder(chat);
		String actual = fileDumpEntryBuilder.getContent();
		
		final String expected = 
			"Chat Id: #42;$foo\n" + 
			"Chat Time: 2011/04/21 15:00:00\n" + 
			"Chat Body Signature: content-id-mock\n" + 
			"Messages signatures: [17f4007f9024da870afae8e60f6635fd,d6bbf5c7f50d1a96fcc3a2156dbf2b86]\n" + 
			"Chat topic: FOO\n" + 
			"Poster: id=joe; display=JOE\n" + 
			"Poster: id=moe; display=MOE\n" + 
			"[2011/04/21 15:14:18] MOE: Hya\n" + 
			"[2011/04/21 15:14:24] JOE: Howdy\n" + 
			"	I'm doing fine";
		
		Assert.assertEquals(expected, actual);
	}

	
	@Test
	public void fileRecorderTest()
	{
		ChatContentBuilderFactory chatEntryBuilderFactory = new FileDumpContentBuilderFactory();
		
		SkypeStorage skypeStorage = new SkypeStorageMock(chatEntryBuilderFactory);
		
		SkypeApiMock skypeApi = new SkypeApiMock();
		skypeApi.addMockChat(SkypeApiMock.produceChatMock("42","moe","joe"));
		skypeApi.addMockChat(SkypeApiMock.produceChatMock("73","john","doe"));
		
		SkypeRecorder skypeRecorder = new SkypeRecorder(skypeApi, skypeStorage);
		skypeRecorder.record();
		
		final String actual = skypeStorage.toString().trim();
		
		String expected = 
				"@SkypeStorageMock:\n" + 
				"@StorageEntryMock: ------\n" + 
				"chatId:42, topic:FOO, date:2011/04/21 15:00:00\n" + 
				"Chat Id: 42\n" + 
				"Chat Time: 2011/04/21 15:00:00\n" + 
				"Chat Body Signature: content-id-mock\n" + 
				"Messages signatures: [17f4007f9024da870afae8e60f6635fd,d6bbf5c7f50d1a96fcc3a2156dbf2b86]\n" + 
				"Chat topic: FOO\n" + 
				"Poster: id=joe; display=JOE\n" + 
				"Poster: id=moe; display=MOE\n" + 
				"[2011/04/21 15:14:18] MOE: Hya\n" + 
				"[2011/04/21 15:14:24] JOE: Howdy\n" + 
				"	I'm doing fine\n" + 
				"Last modified:2011/04/21 15:14:24\n" + 
				"@StorageEntryMock: ------\n" + 
				"chatId:73, topic:FOO, date:2011/04/21 15:00:00\n" + 
				"Chat Id: 73\n" + 
				"Chat Time: 2011/04/21 15:00:00\n" + 
				"Chat Body Signature: content-id-mock\n" + 
				"Messages signatures: [c0687481b3f39aac8fd1ad874f604301,38e57729cade7223217d12bee978a509]\n" + 
				"Chat topic: FOO\n" + 
				"Poster: id=doe; display=DOE\n" + 
				"Poster: id=john; display=JOHN\n" + 
				"[2011/04/21 15:14:18] JOHN: Hya\n" + 
				"[2011/04/21 15:14:24] DOE: Howdy\n" + 
				"	I'm doing fine\n" + 
				"Last modified:2011/04/21 15:14:24";
		Assert.assertEquals(expected , actual);
	}
}
