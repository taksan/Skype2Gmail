package testutils;

import java.util.Date;

import skype.SkypeChatFactoryImpl;
import skype.SkypeChatImpl;
import skype.SkypeChatMessage;
import skype.SkypeChatMessageDataFactory;
import skype.SkypeUserFactory;
import skype.SkypeUserImpl;
import skype.TimeSortedMessages;
import skype.UsersSortedByUserId;
import skype.mocks.SkypeUserFactoryMock;
import utils.DigestProvider;
import utils.SimpleLoggerProvider;

abstract public class SkypeChatBuilderHelper {
	public final DigestProvider digestProvider;
	public final SkypeChatMessageDataFactory skypeChatMessageFactory;
	public final SkypeChatFactoryImpl skypeChatFactoryImpl;
	private  final TimeSortedMessages messageList;

	public SkypeChatBuilderHelper() {
		digestProvider = new DigestProvider();
		skypeChatMessageFactory = new SkypeChatMessageDataFactory(digestProvider);
		SkypeUserFactory skypeUserFactory = new SkypeUserFactoryMock();
		skypeChatFactoryImpl = new SkypeChatFactoryImpl(digestProvider, skypeChatMessageFactory, skypeUserFactory, new SimpleLoggerProvider());
		messageList = new TimeSortedMessages();
	}
	public abstract void addChatMessages();

	public SkypeChatImpl getChat(String chatId, String topic) {
		addChatMessages();
		UsersSortedByUserId members = addPosters();

		Date chatTime = DateHelper.makeDate(2011, 3, 21, 15, 0, 0);
		return (SkypeChatImpl) skypeChatFactoryImpl.produce(chatId, chatTime, topic, members, messageList);
	}
	protected UsersSortedByUserId addPosters() {
		final UsersSortedByUserId userIds = new UsersSortedByUserId();
		boolean isCurrentUser = true;
		for (SkypeChatMessage msg : messageList) {
			userIds.add(new SkypeUserImpl(msg.getSenderId(), msg.getSenderDisplayname(), isCurrentUser));
			isCurrentUser = false;
		}
		return userIds;
	}
	
	public void addMessage(String userId, String message, int month,int day, int hour, int minute, int second) {
		Date firstMessageTime = DateHelper.makeDate(2011, month, day, hour, minute, second);
		SkypeChatMessage firstMessage = skypeChatMessageFactory.produce(userId,
				userId.toUpperCase(), message, firstMessageTime);
		messageList.add(firstMessage);
	}
	
	public void addMessage(String userId, String displayName, String message, int month,int day, int hour, int minute, int second) {
		Date firstMessageTime = DateHelper.makeDate(2011, month, day, hour, minute, second);
		SkypeChatMessage firstMessage = skypeChatMessageFactory.produce(userId,
				displayName, message, firstMessageTime);
		messageList.add(firstMessage);
	}
}