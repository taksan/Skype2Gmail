package skype.mocks;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;

import skype.SkypeChat;
import skype.SkypeChatMessage;
import skype.SkypeChatMessageData;
import skype.TimeSortedMessages;

public class SkypeChatMock implements SkypeChat {

	private final String chatId;
	private final Date chatDate;
	private final TimeSortedMessages messageList = new TimeSortedMessages();
	private final String topic;
	private final List<String> members;

	public SkypeChatMock(String chatId, Date date, String topic,
			String[] members) {
		this.chatId = chatId;
		this.chatDate = date;
		this.topic = topic;
		this.members = Arrays.asList(members);
	}

	@Override
	public TimeSortedMessages getChatMessages() {
		return messageList;
	}

	public SkypeChatMock addMockMessage(String time, String userId,
			String userDisplay, String message) {
		try {
			Date dateTime = SkypeChatMessage.dateFormat.parse(time);
			SkypeChatMessageData msgMock = new SkypeChatMessageData(userId, userDisplay, message, dateTime);
			messageList.add(msgMock);

			return this;
		} catch (ParseException ex) {
			throw new RuntimeException(ex);
		}
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

	@Override
	public String toString(){
		StringBuilder result = new StringBuilder();
		result.append("members: ");
		
		result.append(StringUtils.join(members,","));
		result.append("\n");
		
		result.append(StringUtils.join(messageList,"").trim());
		
		return result.toString();
	}

	@Override
	public String getChatContentId() {
		throw new NotImplementedException();
	}
}
