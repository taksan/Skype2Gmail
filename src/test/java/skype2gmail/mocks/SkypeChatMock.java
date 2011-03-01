package skype2gmail.mocks;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import skype2gmail.SkypeChat;
import skype2gmail.SkypeChatMessage;
import skype2gmail.SkypeChatMessageData;


public class SkypeChatMock implements SkypeChat {

	private final String chatId;
	private final Date chatDate;
	private final List<SkypeChatMessage> messageList = new LinkedList<SkypeChatMessage>();
	private final String topic;
	private final List<String> members;

	public SkypeChatMock(String chatId, Date date, String topic, String[] members) {
		this.chatId = chatId;
		this.chatDate = date;
		this.topic = topic;
		this.members = Arrays.asList(members);
	}

	@Override
	public List<SkypeChatMessage> getChatMessages() {
		return messageList;
	}

	public SkypeChatMock addMockMessage(String msgId, String time, String userId, String userDisplay, String message) throws ParseException {
		Date dateTime = SkypeChatMessage.dateFormat.parse(time);
		SkypeChatMessageData msgMock = new SkypeChatMessageData(msgId, userId, userDisplay, message, dateTime);
		messageList.add(msgMock);
		
		return this;
	}

	@Override
	public String getId() {
		return this.chatId;
	}

	@Override
	public Date getTime() {
		return this.chatDate;
	}

	@Override
	public String getTopic() {
		return this.topic;
	}

	@Override
	public List<String> getMembersIds() {
		return members;
	}

}
