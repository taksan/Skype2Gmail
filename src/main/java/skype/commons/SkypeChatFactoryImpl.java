package skype.commons;

import java.util.Date;

import skype.exceptions.ApplicationException;
import utils.DigestProvider;
import utils.LoggerProvider;

import com.google.inject.Inject;
import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.SkypeException;
import com.skype.User;

public class SkypeChatFactoryImpl implements SkypeChatFactory {

	private final DigestProvider digestProvider;
	private final SkypeChatMessageDataFactory skypeChatMessageFactory;
	private final SkypeUserFactory skypeUserFactory;
	private final LoggerProvider loggerProvider;

	@Inject
	public SkypeChatFactoryImpl(DigestProvider digestProvider,
			SkypeChatMessageDataFactory skypeChatMessageFactory,
			SkypeUserFactory skypeUserFactory, LoggerProvider loggerProvider) {
		this.digestProvider = digestProvider;
		this.skypeChatMessageFactory = skypeChatMessageFactory;
		this.skypeUserFactory = skypeUserFactory;
		this.loggerProvider = loggerProvider;
	}

	@Override
	public SkypeChat produce(Chat chat) {
		try {
			TimeSortedMessages chatMessages = populateChatList(chat);
			UsersSortedByUserId chatPosters = populateUserList(chat,
					chatMessages);

			return new SkypeChatImpl(this.digestProvider, chat.getId(),
					chat.getTime(), chat.getWindowTitle(), chatPosters,
					chatMessages);
		} catch (SkypeException e) {
			throw new ApplicationException(e);
		}
	}

	public SkypeChat produce(String chatId, Date chatTime, String topic,
			UsersSortedByUserId userList, TimeSortedMessages messageList) {
		return new SkypeChatImpl(this.digestProvider, chatId, chatTime, topic,
				userList, messageList);
	}

	UsersSortedByUserId populateUserList(Chat chat,
			TimeSortedMessages chatMessages) throws SkypeException {
		UsersSortedByUserId chatUsers = new UsersSortedByUserId();
		addUsersFromMessages(chatMessages, chatUsers);
		addUsersFromChatPosters(chat, chatUsers);

		return chatUsers;
	}

	private void addUsersFromChatPosters(Chat chat,
			UsersSortedByUserId chatUsers) throws SkypeException {
		User[] allMembers = chat.getAllMembers();
		for (User user : allMembers) {
			String userId = user.getId();
			String fullName = user.getFullName();

			SkypeUserImpl skypeUser = (SkypeUserImpl) skypeUserFactory.produce(
					userId, fullName);
			chatUsers.add(skypeUser);
		}
	}

	private void addUsersFromMessages(TimeSortedMessages chatMessages, UsersSortedByUserId chatUsers) {
		for (SkypeChatMessage skypeChatMessage : chatMessages) {
			SkypeUserImpl skypeUser = (SkypeUserImpl) skypeUserFactory.produce(
					skypeChatMessage.getSenderId(), skypeChatMessage.getSenderDisplayname());
			chatUsers.add(skypeUser);
		}
	}

	private TimeSortedMessages populateChatList(Chat chat)
			throws SkypeException {
		ChatMessage[] allChatMessages = chat.getAllChatMessages();
		TimeSortedMessages chatMessageList = new TimeSortedMessages();
		for (ChatMessage chatMessage : allChatMessages) {
			final SkypeChatMessageData skypeChatMessageData = 
				this.skypeChatMessageFactory.produce(chatMessage);

			chatMessageList.add(skypeChatMessageData);
		}

		return chatMessageList;
	}

	@Override
	public SkypeChat produceEmpty() {
		return new EmptySkypeChat(loggerProvider);
	}

}
