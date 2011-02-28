package skype2gmail.mocks;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import skype2gmail.NotImplementedException;
import skype2gmail.SkypeChat;
import skype2gmail.SkypeChatMessage;
import skype2gmail.SkypeChatMessageMock;


public class SkypeChatMock implements SkypeChat {

	private final String chatId;
	private final Date chatDate;
	private final List<SkypeChatMessageMock> messageList = new LinkedList<SkypeChatMessageMock>();

	public SkypeChatMock(String chatId, Date date) {
		this.chatId = chatId;
		this.chatDate = date;
	}

	@Override
	public List<? extends SkypeChatMessage> getChatMessages() {
		return messageList;
	}

	public SkypeChatMock addMockMessage(String msgId, String time, String userId, String userDisplay, String message) {
		SkypeChatMessageMock msgMock = new SkypeChatMessageMock(msgId, time, userId, userDisplay, message);
		messageList.add(msgMock);
		
		return this;
	}

}
