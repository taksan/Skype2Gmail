package skype;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.SkypeException;
import com.skype.User;

public class SkypeChatImpl implements SkypeChat {

	private List<SkypeChatMessage> chatMessageList;
	private final String chatId;
	private final Date chatTime;
	private final String topic;
	private final List<String> memberIds;

	public SkypeChatImpl(Chat chat) {
		try {
			ChatMessage[] allChatMessages = chat.getAllChatMessages();
			chatId = chat.getId();
			chatTime = chat.getTime();
			topic = chat.getWindowTitle();
			User[] allMembers = chat.getAllMembers();
			memberIds = new LinkedList<String>();
			for (User user : allMembers) {
				memberIds.add(user.getId());
			}
			
			createMessageListFromChatMessages(allChatMessages);
		} catch (SkypeException e) {
			throw new IllegalStateException(e);
		}
	}

	private void createMessageListFromChatMessages(ChatMessage[] allChatMessages) {
		chatMessageList = new LinkedList<SkypeChatMessage>();
		for (ChatMessage chatMessage : allChatMessages) {
			try {
				chatMessageList.add(new SkypeChatMessageData(chatMessage));
			} catch (ParseException e) {
				throw new IllegalStateException(e);
			} catch (SkypeException e) {
				throw new IllegalStateException(e);
			}
		}
	}

	@Override
	public List<SkypeChatMessage> getChatMessages() {
		return chatMessageList;
	}

	@Override
	public String getId() {
		return this.chatId;
	}

	@Override
	public Date getTime() {
		return this.chatTime;
	}

	@Override
	public String getTopic() {
		return this.topic;
	}

	@Override
	public List<String> getMembersIds() {
		return memberIds;
	}

}
