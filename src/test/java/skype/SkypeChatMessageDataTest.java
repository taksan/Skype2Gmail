package skype;

import java.text.ParseException;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

import skype.mocks.SkypeChatMock;
import testutils.DateHelper;

public class SkypeChatMessageDataTest {

	@Test
	public void happyDayTest() throws ParseException {
		Date messageTime = DateHelper.makeDate(2011, 01, 01, 03, 30, 00);
		Date chatStartTime = DateHelper.makeDate(2011, 01, 01, 02, 00, 00);
		SkypeChat skypeChat = new SkypeChatMock(null, chatStartTime, null, new String[0]);
		SkypeChatMessageData skypeChatMessageData = new SkypeChatMessageData(skypeChat , "foosan", "Foo San", "dono foo", messageTime);
		
		
		final String messageWithSender = skypeChatMessageData.messageText(true);
		final String expected = "[03:30:00] Foo San: dono foo\n";
		Assert.assertEquals(expected , messageWithSender);
		
		final String messageWithoutSender = skypeChatMessageData.messageText(false);
		final String expectedWithouSender = "[03:30:00] ... dono foo\n";
		Assert.assertEquals(expectedWithouSender, messageWithoutSender);
		
		final String messageId = skypeChatMessageData.getId();
		final String msgMd5 = "6c87fe864a2b8251203e6edd8fa5f0cf";
		Assert.assertEquals(msgMd5, messageId);
	}
}
