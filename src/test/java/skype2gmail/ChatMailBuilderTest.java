package skype2gmail;

import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import skype2gmail.mocks.SkypeChatMock;

public class ChatMailBuilderTest {

	@Test
	public void happyDayTest() {
		String chatId = "#cardoso.grazieli/$gabrielsan;81ef2618fc9a6343";
		Date chatDate = makeDate(2011, 02, 21, 15, 39, 46);
		SkypeChatMock chat = new SkypeChatMock(chatId, chatDate);

		chat.
			addMockMessage("919", "15:14:18", "gabrielsan", "Gabriel Takeuchi", "qual a instância de homologação?").
			addMockMessage("917", "15:18:16", "cardoso.grazieli", "Grazieli", "as tres").
			addMockMessage("918", "15:14:24", "gabrielsan", "Gabriel Takeuchi", "ng-01, 02 ou 03?");

		ChatMailBuilder chatMailBuilder = new ChatMailBuilder(chat);

		String actual = chatMailBuilder.asString();
		String expected = 
			"Chat [#cardoso.grazieli/$gabrielsan;81ef2618fc9a6343] at 15:39:46 21/02/2011\n" +
			"[919,918,917]\n" +
			"[15:14:18] Gabriel Takeuchi: qual a instância de homologação?\n" +
			"[15:14:24] ... ng-01, 02 ou 03?\n" +
			"[15:18:16] Grazieli: as tres";
		
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
