package skype;

import java.util.Date;
import java.util.List;

import utils.DigestProvider;

public class SkypeChatImpl implements SkypeChat {

	private TimeSortedMessages chatMessageList;
	private final String chatId;
	private final Date chatTime;
	private final String topic;
	private final List<String> memberIds;
	private final DigestProvider digestProvider;
	private String messagesEncodedId;

	public SkypeChatImpl(DigestProvider digestProvider, String chatId, Date chatTime, String topic, List<String> userIds, TimeSortedMessages timeSortedMessages) {
		this.digestProvider = digestProvider;
		this.chatId = chatId;
		this.chatTime = chatTime;
		this.topic = topic;

		memberIds = userIds;
		// populateUserList(chat);

		chatMessageList = timeSortedMessages;
		// populateChatList(chat);
	}
	
	

	@Override
	public List<String> getMembersIds() {
		return memberIds;
	}

	@Override
	public TimeSortedMessages getChatMessages() {
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
	public String getChatContentId() {
		if (this.messagesEncodedId != null) {
			return this.messagesEncodedId;
		}
		this.messagesEncodedId = this.calcIdentifierOfMessagesIds();
		return this.messagesEncodedId;
	}

	private String calcIdentifierOfMessagesIds() {
		String fullChatIds = "";
		int fullChatLen = 0;
		for (SkypeChatMessage aMessage : this.chatMessageList) {
			fullChatIds += aMessage.getId();
			fullChatLen = aMessage.getMessageBody().length();
		}
		final String data = this.chatId+fullChatIds;
		digestProvider.extendedEncode(data);
		return fullChatLen + "#" + digestProvider.extendedEncode(data);
	}

	@Override
	public Date getLastModificationTime() {
		return this.chatMessageList.last().getTime();
	}
}
