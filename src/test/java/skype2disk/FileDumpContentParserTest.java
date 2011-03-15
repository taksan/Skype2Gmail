package skype2disk;

import java.util.Date;

import junit.framework.Assert;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import skype.SkypeChat;
import skype.SkypeChatFactoryImpl;
import skype.SkypeChatImpl;
import skype.SkypeChatMessage;
import skype.SkypeChatMessageDataFactory;
import skype.TimeSortedMessages;
import skype.UsersSortedByUserId;
import testutils.DateHelper;
import testutils.SkypeChatHelper;
import utils.DigestProvider;

public class FileDumpContentParserTest {
	@Test
	public void testParse()
	{
		final DigestProvider digestProvider = new DigestProvider();
		final SkypeChatMessageDataFactory skypeChatMessageFactory = new SkypeChatMessageDataFactory(digestProvider);
		final SkypeChatFactoryImpl skypeChatFactoryImpl = new SkypeChatFactoryImpl(digestProvider, skypeChatMessageFactory);
		
		Date chatTime = DateHelper.makeDate(2011, 3, 21, 15, 0, 0);
		UsersSortedByUserId members= SkypeChatHelper.makeUserList(new String[]{"moe","joe"});
		
		TimeSortedMessages messageList = new TimeSortedMessages();
		addMessage(skypeChatMessageFactory, messageList, 18, "joe", 21, 14, "Hya");
		addMessage(skypeChatMessageFactory, messageList, 18, "moe", 21, 24, "Howdy\n	I'm doing fine");
		
		final String chatId = "#42;$foo";
		final String topic = "FOO";
		SkypeChatImpl chatImpl = (SkypeChatImpl) skypeChatFactoryImpl.produce(chatId, chatTime, topic, members, messageList);
		
		FileDumpContentBuilder fileDumpEntryBuilder = new FileDumpContentBuilder(chatImpl);
		final String fileContents = fileDumpEntryBuilder.getContent();
		
		System.out.println(fileContents);
		
		FileDumpContentParser fileDumpContentParser = new FileDumpContentParserImpl(
				skypeChatFactoryImpl, 
				skypeChatMessageFactory, 
				SkypeChatMessage.chatDateFormat,
				SkypeChatMessage.chatMessageDateFormat);
		
		SkypeChat skypeChat = fileDumpContentParser.parse(fileContents);
		
		Assert.assertEquals(chatId, skypeChat.getId());
		Assert.assertEquals(topic, skypeChat.getTopic());
		
		UsersSortedByUserId membersIds = getSortedUsers(skypeChat);
		
		String users = StringUtils.join(membersIds, "/");
		Assert.assertEquals("id=joe; displayName=JOE/id=moe; displayName=MOE", users);
		
		TimeSortedMessages chatMessages = skypeChat.getChatMessages();
		Assert.assertEquals(2, chatMessages.size());
		SkypeChatMessage lastMessage = chatMessages.last();
		
		Assert.assertEquals("209cfb52f20f1310a2de704eb5ccc0ed", lastMessage.getId());
		Assert.assertEquals("moe", lastMessage.getSenderId());
		Assert.assertEquals("MOE", lastMessage.getSenderDisplayname());
		Assert.assertEquals("Howdy\n	I'm doing fine", lastMessage.getMessageBody());
	}

	private UsersSortedByUserId getSortedUsers(SkypeChat skypeChat) {
		return skypeChat.getMembersIds();
	}

	private void addMessage(
			final SkypeChatMessageDataFactory skypeChatMessageFactory,
			TimeSortedMessages messageList, int second, String userId, int day,
			int minute, String message) {
		Date firstMessageTime = DateHelper.makeDate(2011, 3, day, 15,minute,second);
		SkypeChatMessage firstMessage = skypeChatMessageFactory.produce(userId, userId.toUpperCase(), message, firstMessageTime);
		messageList.add(firstMessage);
	}
}
