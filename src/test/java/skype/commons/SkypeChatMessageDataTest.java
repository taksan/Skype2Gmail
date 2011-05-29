package skype.commons;

import java.text.ParseException;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

import skype.commons.SkypeChatMessageData;
import testutils.DateHelper;
import testutils.DigestProviderForTestFactory;
import utils.DigestProvider;

public class SkypeChatMessageDataTest {

	@Test
	public void happyDayTest() throws ParseException {
		final DigestProvider digestProvider = DigestProviderForTestFactory.getInstance();
		Date messageTime = DateHelper.makeDate(2011, 01, 01, 03, 30, 00);
		SkypeChatMessageData skypeChatMessageData = new SkypeChatMessageData(digestProvider, "foosan", "Foo San", "dono foo", messageTime);
		
		
		final String messageWithSender = skypeChatMessageData.messageText(true);
		final String expected = "[2011/02/01 03:30:00] Foo San: dono foo\n";
		Assert.assertEquals(expected , messageWithSender);
		
		final String messageWithoutSender = skypeChatMessageData.messageText(false);
		final String expectedWithouSender = "[2011/02/01 03:30:00] ... dono foo\n";
		Assert.assertEquals(expectedWithouSender, messageWithoutSender);
		
		final String messageId = skypeChatMessageData.getSignature();
		final String msgContentId = "6c87fe864a2b8251203e6edd8fa5f0cf";
		Assert.assertEquals(msgContentId, messageId);
	}
	
	@Test
	public void testReturnCarriageDoesntChangeSignature()
	{
		final DigestProvider digestProvider = DigestProviderForTestFactory.getInstance();
		Date messageTime = DateHelper.makeDate(2011, 01, 01, 03, 30, 00);
		SkypeChatMessageData chat1 = new SkypeChatMessageData(digestProvider, "foosan", "Foo San", "dono\nfoo", messageTime);
		SkypeChatMessageData chat2 = new SkypeChatMessageData(digestProvider, "foosan", "Foo San", "dono\r\nfoo", messageTime);
		Assert.assertEquals(chat1.getSignature(),chat2.getSignature());
	}
}
