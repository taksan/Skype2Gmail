package skype;

import java.text.ParseException;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import skype.mocks.SkypeChatMock;
import skype2disk.FileDumpBuilder;
import testutils.DateHelper;

public class FileDumpBuilderTest {

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

		FileDumpBuilder chatMailBuilder = new FileDumpBuilder(chat);

		String actual = chatMailBuilder.toChatText();
		String expected = 
			"Chat topic: FOO\n"+
			"Chat [#camaron.goo/$goofoo;81ef2618fc9a6343] at 2011/03/21 15:00:00\n" +
			"Chat members: [camaron.goo,goofoo]\n"+
			"[858000,864000,1096000]\n" +
			"[15:14:18] Goo Foo: what's up\n" +
			"[15:14:24] ... doing fine??\n" +
			"[15:18:16] Camaron: so far so good";
		
		Assert.assertEquals(expected, actual);
	}
}
