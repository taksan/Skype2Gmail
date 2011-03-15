package skype2disk;

import junit.framework.Assert;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import skype.SkypeChat;
import skype.SkypeChatImpl;
import skype.SkypeChatMessage;
import skype.TimeSortedMessages;
import skype.UsersSortedByUserId;
import testutils.SkypeChatTestHelper;

public class FileDumpContentParserTest {
	@Test
	public void testParse() {

		final String chatId = "#42;$foo";
		final String topic = "FOO";

		SkypeChatTestHelper chatHelper = new SkypeChatTestHelper() {

			@Override
			public void addChatMessages() {
				addMessage("moe", "", 21, 14, 10);
				addMessage("joe", "Hya", 21, 14, 18);
				addMessage("joe", "fellow", 21, 15, 18);
				addMessage("moe", "Howdy\n	I'm doing fine", 21, 24, 18);
				addMessage("joe", "A day has passed", 22, 24, 18);
			}
		};

		SkypeChatImpl chatImpl = chatHelper.getChat(chatId, topic);

		FileDumpContentBuilder fileDumpEntryBuilder = new FileDumpContentBuilder(
				chatImpl);
		final String fileContents = fileDumpEntryBuilder.getContent();

		FileDumpContentParser fileDumpContentParser = new FileDumpContentParserImpl(
				chatHelper.skypeChatFactoryImpl,
				chatHelper.skypeChatMessageFactory,
				SkypeChatMessage.chatDateFormat,
				SkypeChatMessage.chatMessageDateFormat);

		SkypeChat skypeChat = fileDumpContentParser.parse(fileContents);

		Assert.assertEquals(chatId, skypeChat.getId());
		Assert.assertEquals(topic, skypeChat.getTopic());

		UsersSortedByUserId membersIds = getSortedUsers(skypeChat);

		String users = StringUtils.join(membersIds, "/");
		Assert.assertEquals("id=joe; displayName=JOE/id=moe; displayName=MOE",
				users);

		TimeSortedMessages chatMessages = skypeChat.getChatMessages();
		Assert.assertEquals(5, chatMessages.size());
		SkypeChatMessage lastMessage = chatMessages.last();

		Assert.assertEquals("501fb6ccae7c8806d56daa1ee89ba949",
				lastMessage.getId());
		Assert.assertEquals("joe", lastMessage.getSenderId());
		Assert.assertEquals("JOE", lastMessage.getSenderDisplayname());
		Assert.assertEquals("A day has passed", lastMessage.getMessageBody());
	}

	@Test
	public void testParsingOnActualText() {
		String chatSample = "Chat Id: #johndoe/$janedoe;7e39ff52e4f97765\n"
				+ "Chat Time: 2011/03/14 13:55:04\n"
				+ "Chat Body Signature: 0#7e4f13a81dae8cd349d1019b9da3e69111a59864a9e3412833e4c6b532e60910\n"
				+ "Messages signatures: [ade6502057cff65dc68ee6aad5a2339e,5b8cb1606fe5d5868d6a62b81d82b6c1,fba8279dcd5f803f6fb762d8e0e2701f]\n"
				+ "Chat topic: Hello boy\n"
				+ "Poster: id=johndoe; display=John Doe\n"
				+ "Poster: id=janedoe; display=Jane Doe\n"
				+ "[2011/03/14 13:55:10] John Doe: oi Renato\n"
				+ "[2011/03/14 14:14:31] Jane Doe: opa\n"
				+ "[2011/03/14 19:33:44] John Doe:";

		SkypeChatTestHelper chatHelper = new SkypeChatTestHelper() {
			@Override
			public void addChatMessages() {
			}
		};

		FileDumpContentParser fileDumpContentParser = new FileDumpContentParserImpl(
				chatHelper.skypeChatFactoryImpl,
				chatHelper.skypeChatMessageFactory,
				SkypeChatMessage.chatDateFormat,
				SkypeChatMessage.chatMessageDateFormat);

		fileDumpContentParser.parse(chatSample);
	}

	private UsersSortedByUserId getSortedUsers(SkypeChat skypeChat) {
		return skypeChat.getMembersIds();
	}

}
