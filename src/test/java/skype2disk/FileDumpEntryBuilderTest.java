package skype2disk;

import java.text.ParseException;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import skype.ChatEntryBuilderFactory;
import skype.SkypeRecorder;
import skype.SkypeStorage;
import skype.mocks.SkypeApiMock;
import skype.mocks.SkypeChatMock;
import skype.mocks.SkypeStorageMock;
import testutils.DateHelper;

public class FileDumpEntryBuilderTest {

	@Test
	public void happyDayTest() throws ParseException {
		String chatId = "#camaron.goo/$goofoo;81ef2618fc9a6343";
		Date chatDate = DateHelper.makeDate(2011, 02, 21, 15, 00, 00);
		String[] members = new String[]{"camaron.goo","goofoo"};
		SkypeChatMock chat = new SkypeChatMock(chatId, chatDate, "FOO", members);

		chat.
			addMockMessage("2011/03/21 15:14:18", "goofoo", "Goo Foo", "what's up").
			addMockMessage("2011/03/21 15:18:16", "camaron.goo", "Camaron", "so far so good").
			addMockMessage("2011/03/21 15:14:24", "goofoo", "Goo Foo", "doing fine??");

		FileDumpEntryBuilder fileDumpEntryBuilder = new FileDumpEntryBuilder(chat);

		String actual = fileDumpEntryBuilder.getContent();
		String expected = 
			"Chat topic: FOO\n"+
			"Chat [#camaron.goo/$goofoo;81ef2618fc9a6343] at 2011/03/21 15:00:00\n" +
			"Chat members: [camaron.goo,goofoo]\n"+
			"Messages Ids: [858000,864000,1096000]\n" +
			"[15:14:18] Goo Foo: what's up\n" +
			"[15:14:24] ... doing fine??\n" +
			"[15:18:16] Camaron: so far so good";
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void fileRecorderTest()
	{
		SkypeApiMock skypeApi = new SkypeApiMock();
		skypeApi.addMockChat(SkypeApiMock.produceChatMock("42","moe","joe"));
		skypeApi.addMockChat(SkypeApiMock.produceChatMock("73","john","doe"));
		
		SkypeStorage skypeStorage = new SkypeStorageMock();
		ChatEntryBuilderFactory chatEntryBuilderFactory = new FileDumpEntryBuilderFactory();
		
		SkypeRecorder skypeRecorder = new SkypeRecorder(skypeApi, skypeStorage, chatEntryBuilderFactory);
		skypeRecorder.record();
		
		String actual = skypeStorage.toString().trim();
		
		String expected = 
				"@SkypeStorageMock:\n" + 
				"@StorageEntryMock: ------\n" + 
				"chatId:42, topic:FOO, date:2011/04/21 15:00:00\n" + 
				"Chat topic: FOO\n" + 
				"Chat [42] at 2011/04/21 15:00:00\n" + 
				"Chat members: [moe,joe]\n" + 
				"Messages Ids: [858000,864000]\n" + 
				"[15:14:18] MOE: Hya\n" + 
				"[15:14:24] JOE: Howdy\n" + 
				"Last modified:2011/04/21 15:14:24\n"+
				"@StorageEntryMock: ------\n" + 
				"chatId:73, topic:FOO, date:2011/04/21 15:00:00\n" + 
				"Chat topic: FOO\n" + 
				"Chat [73] at 2011/04/21 15:00:00\n" + 
				"Chat members: [john,doe]\n" + 
				"Messages Ids: [858000,864000]\n" + 
				"[15:14:18] JOHN: Hya\n" + 
				"[15:14:24] DOE: Howdy\n"+
				"Last modified:2011/04/21 15:14:24";
		Assert.assertEquals(expected , actual);
	}
}
