package skype;

import java.util.Date;

import utils.DigestProvider;

import com.google.inject.Inject;
import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.SkypeException;
import com.skype.User;

public class SkypeChatFactoryImpl implements SkypeChatFactory {
	
	private final DigestProvider digestProvider;
	private final SkypeChatMessageDataFactory skypeChatMessageFactory;

	@Inject
	public SkypeChatFactoryImpl(DigestProvider digestProvider, SkypeChatMessageDataFactory skypeChatMessageFactory)
	{
		this.digestProvider = digestProvider;
		this.skypeChatMessageFactory = skypeChatMessageFactory;
	}

	@Override
	public SkypeChat produce(Chat chat) {
		try {
			return new SkypeChatImpl(
					this.digestProvider,
					chat.getId(), 
					chat.getTime(), 
					chat.getWindowTitle(), 
					populateUserList(chat), 
					populateChatList(chat));
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
	
	private UsersSortedByUserId populateUserList(Chat chat) throws SkypeException {
		User[] allMembers = chat.getAllPosters();
		UsersSortedByUserId chatUsers = new UsersSortedByUserId();
		for (User user : allMembers) {
			SkypeUserImpl skypeUser = new SkypeUserImpl(user.getId(), user.getFullName());
			chatUsers.add(skypeUser);
		}
		return chatUsers;
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
