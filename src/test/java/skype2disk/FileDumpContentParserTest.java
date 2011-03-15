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
	public void testParse()
	{
		
		final String chatId = "#42;$foo";
		final String topic = "FOO";
		
		SkypeChatTestHelper chatHelper = new SkypeChatTestHelper() {
			
			@Override
			public void addChatMessages() {
				addMessage("joe", "Hya", 21, 14, 18);
				addMessage("joe", "fellow", 21, 15, 18);
				addMessage("moe", "Howdy\n	I'm doing fine", 21, 24, 18);
				addMessage("joe", "A day has passed", 22, 24, 18);
			}
		};
		
		SkypeChatImpl chatImpl = chatHelper.getChat(chatId, topic);
		
		FileDumpContentBuilder fileDumpEntryBuilder = new FileDumpContentBuilder(chatImpl);
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
		Assert.assertEquals("id=joe; displayName=JOE/id=moe; displayName=MOE", users);
		
		TimeSortedMessages chatMessages = skypeChat.getChatMessages();
		Assert.assertEquals(4, chatMessages.size());
		SkypeChatMessage lastMessage = chatMessages.last();
		
		Assert.assertEquals("501fb6ccae7c8806d56daa1ee89ba949", lastMessage.getId());
		Assert.assertEquals("joe", lastMessage.getSenderId());
		Assert.assertEquals("JOE", lastMessage.getSenderDisplayname());
		Assert.assertEquals("A day has passed", lastMessage.getMessageBody());
	}

	private UsersSortedByUserId getSortedUsers(SkypeChat skypeChat) {
		return skypeChat.getMembersIds();
	}

}
