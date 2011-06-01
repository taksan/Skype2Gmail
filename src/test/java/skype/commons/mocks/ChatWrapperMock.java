package skype.commons.mocks;

import java.util.Date;

import skype.commons.UserWrapper;
import skypeapi.wrappers.ChatMessageWrapper;
import skypeapi.wrappers.ChatWrapper;

public class ChatWrapperMock implements ChatWrapper {

	private String chatId;
	private ChatMessageWrapper[] chatMessages;
	private UserWrapper[] members;
	private Date chatTime;

	public ChatWrapperMock(String chatId,Date chatTime) {
		this.chatId = chatId;
		this.chatTime = chatTime;
	}

	@Override
	public ChatMessageWrapper[] getAllChatMessages() {
		return chatMessages;
	}

	@Override
	public Date getTime() {
		return chatTime;
	}

	@Override
	public String getWindowTitle() {
		return "CHAT TOPIC";
	}

	@Override
	public UserWrapper[] getAllMembers() {
		return members;
	}

	@Override
	public String getId() {
		return chatId;
	}

	public void setChatMessages(ChatMessageWrapperMock ...chatMessages) {
		this.chatMessages = chatMessages;
	}

	public void setMembers(UserWrapperMock ...users) {
		members = users;
	}

}
