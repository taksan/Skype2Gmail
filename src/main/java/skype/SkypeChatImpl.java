package skype;

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
			chatId = chat.getId();
			chatTime = chat.getTime();
			topic = chat.getWindowTitle();

			memberIds = new LinkedList<String>();
			populateUserList(chat);
			
			chatMessageList = new LinkedList<SkypeChatMessage>();
			populateChatList(chat);
		} catch (SkypeException e) {
			throw new IllegalStateException(e);
		}
	}
	
	@Override
	public List<String> getMembersIds() {
		return memberIds;
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

	private void populateUserList(Chat chat) throws SkypeException {
		User[] allMembers = chat.getAllMembers();
		for (User user : allMembers) {
			memberIds.add(user.getId());
		}
	}

	private void populateChatList(Chat chat) throws SkypeException {
		ChatMessage[] allChatMessages = chat.getAllChatMessages();
		try {
			for (ChatMessage chatMessage : allChatMessages) {
				chatMessageList.add(new SkypeChatMessageData(this, chatMessage));
			}
			
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
}
