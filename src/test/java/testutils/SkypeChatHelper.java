package testutils;

import java.util.Date;

import skype.mocks.SkypeChatMock;

public class SkypeChatHelper {
	public static SkypeChatMock createSkypeTestEnvironment() {
		String chatId = "#camaron.goo/$goofoo;81ef2618fc9a6343";
		Date chatDate = DateHelper.makeDate(2011, 02, 21, 15, 00, 00);
		String[] members = new String[] { "camaron.goo", "goofoo" };
		SkypeChatMock chat = new SkypeChatMock(chatId, chatDate, "FOO", members);

		chat.addMockMessage("2011/03/21 15:14:18", "goofoo", "Goo Foo",
				"what's up")
				.addMockMessage("2011/03/21 15:18:16", "camaron.goo",
						"Camaron", "so far so good")
				.addMockMessage("2011/03/21 15:14:24", "goofoo", "Goo Foo",
						"doing fine??");
		return chat;
	}
}
