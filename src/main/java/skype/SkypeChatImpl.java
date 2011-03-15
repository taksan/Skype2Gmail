package skype;

import java.util.Date;

import org.apache.commons.lang.NotImplementedException;

import utils.DigestProvider;

public class SkypeChatImpl implements SkypeChat {

	private TimeSortedMessages chatMessageList;
	private final UsersSortedByUserId memberIds;
	
	private final String chatId;
	private final Date chatTime;
	private final String topic;
	private final DigestProvider digestProvider;
	private String bodySignature;

	public SkypeChatImpl(DigestProvider digestProvider, String chatId, Date chatTime, String topic, UsersSortedByUserId userIds, TimeSortedMessages timeSortedMessages) {
		this.digestProvider = digestProvider;
		this.chatId = chatId;
		this.chatTime = chatTime;
		this.topic = topic;

		memberIds = userIds;
		chatMessageList = timeSortedMessages;
	}
	
	

	@Override
	public UsersSortedByUserId getMembersIds() {
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
	public String getBodySignature() {
		if (this.bodySignature != null) {
			return this.bodySignature;
		}
		this.bodySignature = this.calcBodySignature();
		return this.bodySignature;
	}

	private String calcBodySignature() {
		String concatenatedMessagesSignatures = "";
		int fullChatLen = 0;
		for (SkypeChatMessage aMessage : this.chatMessageList) {
			concatenatedMessagesSignatures += aMessage.getSignature();
			fullChatLen = aMessage.getMessageBody().length();
		}
		final String data = this.chatId+concatenatedMessagesSignatures;
		digestProvider.extendedEncode(data);
		return fullChatLen + "#" + digestProvider.extendedEncode(data);
	}

	@Override
	public Date getLastModificationTime() {
		return this.chatMessageList.last().getTime();
	}



	@Override
	public SkypeChat merge(SkypeChat skypeChat) {
		throw new NotImplementedException();
	}
}
