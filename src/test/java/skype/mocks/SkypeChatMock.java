package skype.mocks;

import java.util.Date;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;

import skype.SkypeChat;
import skype.SkypeChatMessage;
import skype.SkypeChatMessageData;
import skype.SkypeUser;
import skype.TimeSortedMessages;
import skype.UsersSortedByUserId;
import testutils.DigestProviderForTestFactory;
import testutils.SkypeChatHelper;
import utils.DigestProvider;

public class SkypeChatMock implements SkypeChat {

	private final String chatId;
	private final Date chatDate;
	private final TimeSortedMessages messageList = new TimeSortedMessages();
	private final String topic;
	private final UsersSortedByUserId members;
	private final DigestProvider digestProvider;
	private final String bodySignature;

	public SkypeChatMock(String chatId, Date date, String topic,
			String[] members) {
		this(chatId,date,topic,members,"content-id-mock");
	}

	public SkypeChatMock(String chatId, Date date, String topic,
			String[] members, String bodySignature) {
		digestProvider = DigestProviderForTestFactory.getInstance();
		this.chatId = chatId;
		this.chatDate = date;
		this.topic = topic;
		this.members = SkypeChatHelper.makeUserList(members);
		this.bodySignature = bodySignature;
	}

	@Override
	public TimeSortedMessages getChatMessages() {
		return messageList;
	}

	public SkypeChatMock addMockMessage(String time, String userId,
			String userDisplay, String message) {
			Date dateTime = SkypeChatMessage.chatDateFormat.parse(time);
			SkypeChatMessageData msgMock = new SkypeChatMessageData(digestProvider, userId, userDisplay, message, dateTime);
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
	public UsersSortedByUserId getPosters() {
		return members;
	}

	@Override
	public String toString(){
		StringBuilder result = new StringBuilder();
		result.append("members: ");
		
		result.append(StringUtils.join(members,","));
		result.append("\n");
		
		result.append(StringUtils.join(messageList, "").trim());

		return result.toString();
	}

	@Override
	public String getBodySignature() {
		return this.bodySignature;
	}

	@Override
	public Date getLastModificationTime() {
		return this.messageList.last().getTime();
	}

	@Override
	public SkypeChat merge(SkypeChat skypeChat) {
		throw new NotImplementedException();
	}

	@Override
	public SkypeUser getChatAuthor() {
		return new EmptySkypeUser();
	}
}
