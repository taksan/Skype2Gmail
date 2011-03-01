package skype2gmail;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import skype.ChatMailBuilder;
import skype2gmail.mocks.SkypeChatMock;

public class ChatMailBuilderTest {

	@Test
	public void happyDayTest() throws ParseException {
		String chatId = "#camaron.goo/$goofoo;81ef2618fc9a6343";
		Date chatDate = makeDate(2011, 02, 21, 15, 39, 46);
		String[] members = new String[]{"camaron.goo","goofoo"};
		SkypeChatMock chat = new SkypeChatMock(chatId, chatDate, "FOO", members);

		chat.
			addMockMessage("919", "2011/02/21 15:14:18", "goofoo", "Goo Foo", "what's up").
			addMockMessage("917", "2011/02/21 15:18:16", "camaron.goo", "Camaron", "so far so good").
			addMockMessage("918", "2011/02/21 15:14:24", "goofoo", "Goo Foo", "doing fine??");

		ChatMailBuilder chatMailBuilder = new ChatMailBuilder(chat);

		String actual = chatMailBuilder.toChatText();
		String expected = 
			"Chat topic: FOO\n"+
			"Chat [#camaron.goo/$goofoo;81ef2618fc9a6343] at 2011/03/21 15:39:46\n" +
			"Chat members: [camaron.goo,goofoo]\n"+
			"[919,918,917]\n" +
			"[15:14:18] Goo Foo: what's up\n" +
			"[15:14:24] ... doing fine??\n" +
			"[15:18:16] Camaron: so far so good";
		
		Assert.assertEquals(expected, actual);
	}

	public Date makeDate(int year, int month, int day, int hour, int minute,
			int second) {
		Calendar cal = Calendar.getInstance();

		// Clear all fields
		cal.clear();

		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DATE, day);

		cal.set(Calendar.HOUR, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);

		return cal.getTime();
	}
}
