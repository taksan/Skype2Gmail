package skype2disk;

import junit.framework.Assert;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import skype.SkypeChat;
import skype.SkypeChatImpl;
import skype.SkypeChatMessage;
import skype.TimeSortedMessages;
import skype.UsersSortedByUserId;
import testutils.SkypeChatBuilderHelper;

public class FileDumpContentParserTest {
	@Test
	public void testParse() {

		final String chatId = "#42;$foo";
		final String topic = "FOO";

		SkypeChatBuilderHelper chatHelper = new SkypeChatBuilderHelper() {

			@Override
			public void addChatMessages() {
				addMessage("moe", "", 21, 15, 14, 10);
				addMessage("joe", "Hya : ) ...", 21, 15, 14, 18);
				addMessage("joe", "fellow", 21, 15, 15, 18);
				addMessage("joe", "", 21, 15, 15, 19);
				addMessage("moe", "Howdy\n	I'm doing fine", 21, 15, 24, 18);
				addMessage("moe", "\n[2011/03/16 11:49:10] bla said", 21, 15, 24, 19);
				addMessage("joe", "A day has passed", 22, 15, 24, 20);
			}
		};

		SkypeChatImpl chatImpl = chatHelper.getChat(chatId, topic);

		final FileDumpContentBuilder fileDumpEntryBuilder = new FileDumpContentBuilder(chatImpl);
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
		Assert.assertEquals(7, chatMessages.size());
		SkypeChatMessage lastMessage = chatMessages.last();

		Assert.assertEquals("501fb6ccae7c8806d56daa1ee89ba949",
				lastMessage.getSignature());
		Assert.assertEquals("joe", lastMessage.getSenderId());
		Assert.assertEquals("JOE", lastMessage.getSenderDisplayname());
		Assert.assertEquals("A day has passed", lastMessage.getMessageBody());
	}

	@Test
	public void testParsingOnActualText() {
		String chatSample = "Chat Id: #johndoe/$janedoe;7e39ff52e4f97765\n"
				+ "Chat Time: 2011/03/14 13:55:04\n"
				+ "Chat Body Signature: 0#c47ccd1d42e3bc5e5b39168bfe9eb2a5f057aae4a3d2f4b9cdb3e702f58689cb\n"
				+ "Messages signatures: [20c5b66ed09f7fd3ca1baa945c444be1,093b41d317455d08d55838071ff9f5c3,9d0232e74cfa414b3ec3e904f52295de,9d0232e74cfa414b3ec3e904f52295de]\n"
				+ "Chat topic: Hello boy\n"
				+ "Poster: id=john.doe; display=John Doe\n"
				+ "Poster: id=jane.doe; display=Jane Doe\n"
				+ "[2011/03/14 13:55:10] John Doe: oi Jane\n"
				+ "[2011/03/14 14:14:31] Jane Doe: opa\n"
				+ "[2011/03/14 19:33:44] John Doe:\n"
				+ "[2011/03/14 19:33:44] ...";

		testParserOnMessage(chatSample);
	}

	private void testParserOnMessage(String chatSample) {
		SkypeChatBuilderHelper chatHelper = new SkypeChatBuilderHelper() {
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
