package skype2disk;

import junit.framework.Assert;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import skype.SkypeChat;
import skype.SkypeChatImpl;
import skype.SkypeChatMessage;
import skype.SkypeChatWithBodyParserFactory;
import skype.SkypeUserFactory;
import skype.TimeSortedMessages;
import skype.UsersSortedByUserId;
import skype.mocks.SkypeUserFactoryMock;
import testutils.SkypeChatBuilderHelper;

public class FileDumpContentParserTest {
	@Test
	public void testParse() {

		final String chatId = "#42;$foo";
		final String topic = "FOO";

		SkypeChatBuilderHelper chatHelper = new SkypeChatBuilderHelper() {

			@Override
			public void addChatMessages() {
				addMessage("moe", "", 3, 21, 15, 14, 10);
				addMessage("joe", "Hya : ) ...", 3, 21, 15, 14, 18);
				addMessage("joe", "fellow", 3, 21, 15, 15, 18);
				addMessage("joe", "", 3, 21, 15, 15, 19);
				addMessage("moe", "Howdy\n	I'm doing fine", 3, 21, 15, 24, 18);
				addMessage("moe", "\n[2011/03/16 11:49:10] bla said", 3, 21, 15, 24, 19);
				addMessage("joe", "A day has passed", 3, 22, 15, 24, 20);
			}
		};

		SkypeChat skypeChat = buildTextAndParseBackToSkypeChat(chatId, topic, chatHelper);

		Assert.assertEquals(chatId, skypeChat.getId());
		Assert.assertEquals(topic, skypeChat.getTopic());

		UsersSortedByUserId membersIds = getSortedUsers(skypeChat);

		String users = StringUtils.join(membersIds, "/");
		Assert.assertEquals("id=joe; displayName=JOE/id=moe; displayName=MOE",
				users);

		TimeSortedMessages chatMessages = skypeChat.getChatMessages();
		Assert.assertEquals(7, chatMessages.size());
		SkypeChatMessage lastMessage = chatMessages.last();

		Assert.assertEquals("501fb6ccae7c8806d56daa1ee89ba949", lastMessage.getSignature());
		Assert.assertEquals("joe", lastMessage.getSenderId());
		Assert.assertEquals("JOE", lastMessage.getSenderDisplayname());
		Assert.assertEquals("A day has passed", lastMessage.getMessageBody());
	}
	
	@Test
	public void testParseAndEscaping() {

		final String chatId = "#42;$foo";
		final String topic = "FOO";

		SkypeChatBuilderHelper chatHelper = new SkypeChatBuilderHelper() {

			@Override
			public void addChatMessages() {
				addMessage("moe", "\n[2011/03/16 11:49:10] bla said", 3, 21, 15, 24, 19);
				addMessage("moe", "\n[11:49:10] bla said", 3, 21, 15, 24, 20);
			}
		};

		SkypeChat parsedChat = buildTextAndParseBackToSkypeChat(chatId, topic, chatHelper);

		final FileDumpContentBuilder parsedChatBuilder = new FileDumpContentBuilder(parsedChat);
		
		String expected = "Chat Id: #42;$foo\n" + 
				"Chat Time: 2011/04/21 15:00:00\n" + 
				"Chat Body Signature: 76ec9465d068ca51035fa9e083851f3b107e1ee87dd98ea7c6a092ea5e5429b9\n" + 
				"Messages signatures: [e134071f020f5efaf163415317580194,055bb32f383cc8d37656f9223e704b95]\n" + 
				"Chat topic: FOO\n" + 
				"Poster: id=joe; display=JOE\n" + 
				"Poster: id=moe; display=MOE\n" + 
				"[2011/04/21 15:24:19] MOE: \n" + 
				"\\[2011/03/16 11:49:10] bla said\n" + 
				"[2011/04/21 15:24:20] ... \n" + 
				"[11:49:10] bla said";
		
		Assert.assertEquals(expected, parsedChatBuilder.getContent());
	}

	private SkypeChat buildTextAndParseBackToSkypeChat(final String chatId,
			final String topic, SkypeChatBuilderHelper chatHelper) {
		SkypeChatImpl chatImpl = chatHelper.getChat(chatId, topic);
		final FileDumpContentBuilder fileDumpEntryBuilder = new FileDumpContentBuilder(chatImpl);
		final String fileContents = fileDumpEntryBuilder.getContent();
		FileDumpContentParser fileDumpContentParser = createDumpParser(chatHelper);
		SkypeChat parsedChat = fileDumpContentParser.parse(fileContents);
		return parsedChat;
	}


	@Test
	public void testParsingOnActualText() {
		String chatSample = "Chat Id: #johndoe/$janedoe;7e39ff52e4f97765\n"
				+ "Chat Time: 2011/03/14 13:55:04\n"
				+ "Chat Body Signature: 859cffb72c25fa7a9c2bd3fdb7816a54bbcfb4f59406fb36bf80f89d7f32486e\n"
				+ "Messages signatures: [20c5b66ed09f7fd3ca1baa945c444be1,093b41d317455d08d55838071ff9f5c3,2a4bc2c53cb2fe8cf01b545398140ae4]\n"
				+ "Chat topic: Hello boy\n"
				+ "Poster: id=john.doe; display=John Doe\n"
				+ "Poster: id=jane.doe; display=Jane Doe\n"
				+ "[2011/03/14 13:55:10] John Doe: oi Jane\r\n"
				+ "[2011/03/14 14:14:31] Jane Doe: opa\r\n"
				+ "[2011/03/14 19:33:44] john.doe: so\r\n"
				+ "fine";

		testParserOnMessage(chatSample);
	}

	private void testParserOnMessage(String chatSample) {
		SkypeChatBuilderHelper chatHelper = new SkypeChatBuilderHelper() {
			@Override
			public void addChatMessages() {
			}
		};

		FileDumpContentParser fileDumpContentParser = createDumpParser(chatHelper);
		fileDumpContentParser.parse(chatSample);
	}

	private UsersSortedByUserId getSortedUsers(SkypeChat skypeChat) {
		return skypeChat.getPosters();
	}

	private FileDumpContentParser createDumpParser(
			SkypeChatBuilderHelper chatHelper) {
		
		MessageBodyParserFactory messageBodyParserFactory = 
			new MessageBodyParserFactoryImpl(chatHelper.skypeChatMessageFactory, SkypeChatMessage.chatMessageDateFormat);
		
		SkypeUserFactory skypeUserFactory = new SkypeUserFactoryMock();
		SkypeChatWithBodyParserFactory skypeChatWithBodyParserFactory = new SkypeChatWithBodyParserFactory(
				chatHelper.skypeChatFactoryImpl, messageBodyParserFactory, skypeUserFactory);
		
		FileDumpContentParser fileDumpContentParser = new FileDumpContentParserImpl(
				SkypeChatMessage.chatDateFormat,
				skypeChatWithBodyParserFactory);
		return fileDumpContentParser;
	}
}
