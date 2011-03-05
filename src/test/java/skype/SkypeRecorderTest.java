package skype;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

import skype.mocks.ChatEntryBuilderFactoryMock;
import skype.mocks.SkypeApiMock;
import skype.mocks.SkypeChatMock;
import skype.mocks.SkypeStorageMock;
import testutils.DateHelper;

public class SkypeRecorderTest {
	
	@Test
	public void happyDay()
	{
		SkypeApiMock skypeApi = new SkypeApiMock();
		skypeApi.addMockChat(produceChatMock("42","moe","joe"));
		skypeApi.addMockChat(produceChatMock("73","john","doe"));
		
		SkypeStorage skypeStorage = new SkypeStorageMock();
		ChatEntryBuilderFactory chatEntryBuilderFactory = new ChatEntryBuilderFactoryMock();
		
		SkypeRecorder skypeRecorder = new SkypeRecorder(skypeApi, skypeStorage, chatEntryBuilderFactory);
		skypeRecorder.record();
		
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
	
	private SkypeChatMock produceChatMock(String chatId, String member1, String member2) {
		String[] members= new String[]{member1,member2};
		
		Date aDate = DateHelper.makeDate(2011, 3, 21, 15, 0, 0);
		
		SkypeChatMock chat = new SkypeChatMock(chatId, aDate, "FOO", members);
		
		createMockMessage(member1, chat, "2011/03/21 15:14:18", "Hya");
		createMockMessage(member2, chat, "2011/03/21 15:14:24", "Howdy");
		
		return chat;
	}

	private SkypeChatMock createMockMessage(String memberId, SkypeChatMock chat,
			String time, String message) {
		return chat.addMockMessage(time, memberId, memberId.toUpperCase(), message);
	}

}
