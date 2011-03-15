package testutils;

import java.util.Date;

import skype.SkypeChatFactoryImpl;
import skype.SkypeChatImpl;
import skype.SkypeChatMessage;
import skype.SkypeChatMessageDataFactory;
import skype.TimeSortedMessages;
import skype.UsersSortedByUserId;
import utils.DigestProvider;

abstract public class SkypeChatTestHelper {
	public final DigestProvider digestProvider;
	public final SkypeChatMessageDataFactory skypeChatMessageFactory;
	public final SkypeChatFactoryImpl skypeChatFactoryImpl;
	private  final TimeSortedMessages messageList;

	public SkypeChatTestHelper() {
		digestProvider = new DigestProvider();
		skypeChatMessageFactory = new SkypeChatMessageDataFactory(digestProvider);
		skypeChatFactoryImpl = new SkypeChatFactoryImpl(digestProvider, skypeChatMessageFactory);
		messageList = new TimeSortedMessages();
	}
	public abstract void addChatMessages();

	public SkypeChatImpl getChat(String chatId, String topic) {
		UsersSortedByUserId members = SkypeChatHelper.makeUserList(new String[] { "moe", "joe" });
		addChatMessages();

		Date chatTime = DateHelper.makeDate(2011, 3, 21, 15, 0, 0);
		return (SkypeChatImpl) skypeChatFactoryImpl.produce(chatId, chatTime, topic, members, messageList);
	}


	protected void addMessage(String userId, String message, int day,int minute, int second) {
		Date firstMessageTime = DateHelper.makeDate(2011, 3, day, 15, minute,
				second);
		SkypeChatMessage firstMessage = skypeChatMessageFactory.produce(userId,
				userId.toUpperCase(), message, firstMessageTime);
		messageList.add(firstMessage);
	}
}