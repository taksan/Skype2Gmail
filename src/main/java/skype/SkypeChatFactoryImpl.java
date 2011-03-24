package skype;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

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
	public SkypeChatFactoryImpl(
			DigestProvider digestProvider, 
			SkypeChatMessageDataFactory skypeChatMessageFactory,
			SkypeUserFactory skypeUserFactory,
			LoggerProvider loggerProvider)
	{
		this.digestProvider = digestProvider;
		this.skypeChatMessageFactory = skypeChatMessageFactory;
		this.skypeUserFactory = skypeUserFactory;
		this.loggerProvider = loggerProvider;
	}

	@Override
	public SkypeChat produce(Chat chat) {
		try {
			TimeSortedMessages chatMessages = populateChatList(chat);
			UsersSortedByUserId chatPosters = populateUserList(chat, chatMessages);
			
			return new SkypeChatImpl(
					this.digestProvider,
					chat.getId(), 
					chat.getTime(), 
					chat.getWindowTitle(), 
					chatPosters, 
					chatMessages);
		} catch (SkypeException e) {
			throw new IllegalStateException(e);
		}
	}
	
	public SkypeChat produce(String chatId, Date chatTime, String topic, UsersSortedByUserId userList, TimeSortedMessages messageList) {
		return new SkypeChatImpl(
				this.digestProvider,
				chatId, 
				chatTime, 
				topic, 
				userList, 
				messageList);
	}
	
	private UsersSortedByUserId populateUserList(Chat chat, TimeSortedMessages chatMessages) throws SkypeException {
		UsersSortedByUserId chatUsers = new UsersSortedByUserId();
		Map<String, String> users = extractPostersFromMessages(chatMessages);
		for (String userId : users.keySet()) {
			SkypeUserImpl skypeUser = (SkypeUserImpl) skypeUserFactory.produce(userId, users.get(userId));
			chatUsers.add(skypeUser);
		}
		User[] allMembers = chat.getAllMembers();
		for (User user : allMembers) {
			String userId = user.getId();
			String fullName = user.getFullName();
			if (fullName == null)
				fullName = userId;
			
			SkypeUserImpl skypeUser = (SkypeUserImpl) skypeUserFactory.produce(userId, fullName);
			chatUsers.add(skypeUser);
		}
		
		return chatUsers;
	}

	private Map<String, String> extractPostersFromMessages(
			TimeSortedMessages chatMessages) {
		Map<String, String> users = new LinkedHashMap<String, String>();
		for (SkypeChatMessage skypeChatMessage : chatMessages) {
			users.put(skypeChatMessage.getSenderId(), skypeChatMessage.getSenderDisplayname());
		}
		return users;
	}

	private TimeSortedMessages populateChatList(Chat chat) throws SkypeException {
		ChatMessage[] allChatMessages = chat.getAllChatMessages();
		TimeSortedMessages chatMessageList = new TimeSortedMessages();
		try {
			for (ChatMessage chatMessage : allChatMessages) {
				final SkypeChatMessageData skypeChatMessageData =
					this.skypeChatMessageFactory.produce(chatMessage);
				
				chatMessageList.add(skypeChatMessageData);
			}

		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
		return chatMessageList;
	}

	@Override
	public SkypeChat produceEmpty() {
		return new EmptySkypeChat(loggerProvider);
	}

}
