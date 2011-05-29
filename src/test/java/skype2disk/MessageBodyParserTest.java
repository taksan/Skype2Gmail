package skype2disk;

import org.junit.Assert;
import org.junit.Test;

import skype.commons.SkypeChatMessageDataFactory;
import skype.commons.SkypeMessageDateFormat;
import skype.commons.SkypeMessageDateFormatImpl;
import skype.commons.SkypeMessageParsingException;
import skype.commons.SkypeUserImpl;
import skype.commons.UsersSortedByUserId;
import utils.DigestProvider;

public class MessageBodyParserTest {
	@Test
	public void testMalformedMessageNotMatchingAmount() {
		String[] messageSignatures = new String[]{};
		MessageBodyParser parser = makeSubject(new UsersSortedByUserId(), messageSignatures);
		
		String chatSample = "[2011/03/14 13:55:10] John Doe: oi Jane\r\n";
		
		try {
			parser.parse(chatSample);
			Assert.fail("Should have thrown an exception");
		}
		catch (SkypeMessageParsingException e) {
			Assert.assertEquals("Malformed message! Amount of messages doesn't match amount of signatures", e.getMessage());
		}
	}
	
	@Test
	public void testMalformedMessagePosterNotFound() {
		String[] messageSignatures = new String[]{"foo"};
		MessageBodyParser parser = makeSubject(new UsersSortedByUserId(), messageSignatures);
		
		String chatSample = "[2011/03/14 13:55:10] John Doe: oi Jane\r\n";
		
		try {
			parser.parse(chatSample);
			Assert.fail("Should have thrown an exception");
		}
		catch (SkypeMessageParsingException e) {
			Assert.assertEquals("User John Doe was found on chat, but was not among its posters!", e.getMessage());
		}
	}
	
	@Test
	public void testMalformedMessageMismatchingSignature() {
		String[] messageSignatures = new String[]{"foo"};
		UsersSortedByUserId userList = new UsersSortedByUserId();
		userList.add(new SkypeUserImpl("john", "John Doe", false));
		MessageBodyParser parser = makeSubject(userList, messageSignatures);
		
		String chatSample = "[2011/03/14 13:55:10] John Doe: oi Jane\r\n";
		
		try {
			parser.parse(chatSample);
			Assert.fail("Should have thrown an exception");
		}
		catch (SkypeMessageParsingException e) {
			Assert.assertTrue(e.getMessage().startsWith("Message signature doesn't match original"));
		}
	}

	private MessageBodyParser makeSubject(UsersSortedByUserId userList,
			String[] messageSignatures) {
		SkypeChatMessageDataFactory skypeChatMessageDataFactory = new SkypeChatMessageDataFactory(new DigestProvider());
		SkypeMessageDateFormat skypeMessageDateFormat = new SkypeMessageDateFormatImpl();
		MessageBodyParser parser = new MessageBodyParser(skypeChatMessageDataFactory, skypeMessageDateFormat, userList, messageSignatures);
		return parser;
	}
}
