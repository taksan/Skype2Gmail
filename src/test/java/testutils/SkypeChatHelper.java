package testutils;

import java.util.Date;

import skype.SkypeUserImpl;
import skype.UsersSortedByUserId;
import skype.mocks.SkypeChatMock;

public class SkypeChatHelper {

	public static SkypeChatMock createSkypeTestEnvironment() {
		final String chatId = "#camaron.goo/$goofoo;81ef2618fc9a6343";
		final Date chatDate = getDefaultChatTime();
		final String[] members = new String[] { "camaron.goo", "goofoo" };
		final SkypeChatMock chat = new SkypeChatMock(chatId, chatDate, "FOO", members);

		chat.addMockMessage("2011/03/21 15:14:18", "goofoo", "Goo Foo", "what's up")
			.addMockMessage("2011/03/21 15:18:16", "camaron.goo", "Camaron", "so far so good")
			.addMockMessage("2011/03/21 15:14:24", "goofoo", "Goo Foo", "doing fine??");
		return chat;
	}
	
	public static UsersSortedByUserId makeUserList(String[] users) {
		final UsersSortedByUserId userIds = new UsersSortedByUserId();
		for(String user : users) {
			userIds.add(new SkypeUserImpl(user, user.toUpperCase(), false));
		}
		return userIds;
	}
	

	private static Date getDefaultChatTime() {
		return DateHelper.makeDate(2011, 02, 21, 15, 00, 00);
	}
}
