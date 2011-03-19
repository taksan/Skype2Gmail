package skype;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import utils.DigestProvider;

import com.google.inject.Inject;
import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.SkypeException;

public class SkypeChatFactoryImpl implements SkypeChatFactory {
	
	private final DigestProvider digestProvider;
	private final SkypeChatMessageDataFactory skypeChatMessageFactory;
	private final SkypeUserFactory skypeUserFactory;

	@Inject
	public SkypeChatFactoryImpl(
			DigestProvider digestProvider, 
			SkypeChatMessageDataFactory skypeChatMessageFactory,
			SkypeUserFactory skypeUserFactory)
	{
		this.digestProvider = digestProvider;
		this.skypeChatMessageFactory = skypeChatMessageFactory;
		this.skypeUserFactory = skypeUserFactory;
	}

	@Override
	public SkypeChat produce(Chat chat) {
		try {
			TimeSortedMessages chatMessages = populateChatList(chat);
			UsersSortedByUserId chatPosters = populateUserList(chatMessages);
			
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
	
	private UsersSortedByUserId populateUserList(TimeSortedMessages chatMessages) throws SkypeException {
		UsersSortedByUserId chatUsers = new UsersSortedByUserId();
		Map<String, String> users = extractPostersFromMessages(chatMessages);
		for (String userId : users.keySet()) {
			SkypeUserImpl skypeUser = (SkypeUserImpl) skypeUserFactory.produce(userId, users.get(userId));
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

}
