package skype;

import java.util.Date;

import utils.DigestProvider;

import com.skype.ChatMessage;
import com.skype.SkypeException;

public class SkypeChatMessageData implements SkypeChatMessage {
	
	private final String msgId;
	private final String userDisplay;
	private final String message;
	private final Date date;
	private final String userId;
	private final DigestProvider digestProvider;
	public SkypeChatMessageData(DigestProvider digestProvider, ChatMessage chatMessage) throws SkypeException {
		this(
			digestProvider,			
			chatMessage.getSenderId(),
			chatMessage.getSenderDisplayName(),
			chatMessage.getContent(),
			chatMessage.getTime()
			);
	}
	
	public SkypeChatMessageData(DigestProvider digestProvider, String userId,
			String userDisplay, String message, Date time) {
		this.digestProvider = digestProvider;
		if(userId == null || userDisplay == null || message == null || time == null)
			throw new IllegalArgumentException("None of the message arguments can be null");
		
		final String encodingData = userId+"/"+message.replaceAll("[\r]", "");
		this.msgId = digestProvider.encode(encodingData);
		this.userDisplay = userDisplay;
		this.message = message;
		this.date = time;
		this.userId = userId;
	}

	@Override
	public String getSenderDisplayname() {
		return this.userDisplay;
	}

	@Override
	public String getMessageBody() {
		return this.message;
	}

	@Override
	public String getSignature() {
		return this.msgId;
	}

	@Override
	public Date getTime() {
		return this.date;
	}

	@Override
	public String getSenderId() {
		return this.userId;
	}

	@Override
	public String messageText(boolean printSender) {
		String formattedTime = SkypeChatMessage.chatMessageDateFormat.format(this.getTime());
		final String senderDisplayName;
		if (printSender)
			senderDisplayName = this.getSenderDisplayname()+":";
		else
			senderDisplayName = "...";
		
		return String.format("%s %s %s\n",
				formattedTime,
				senderDisplayName,
				this.getMessageBody()
				);
	}

	@Override
	public String toString() {
		return this.messageText(true);
	}
	
	public int hashCode() {
		return this.getSignature().hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		return this.hashCode() == other.hashCode();
	}

	@Override
	public boolean isMatchingSignature(String signature) {
		return this.getSignature().equals(signature);
	}

	@Override
	public boolean earlierThan(SkypeChatMessage equivalentMessageInOtherChat) {
		Date otherDate = equivalentMessageInOtherChat.getTime();
		Date thisDate = this.getTime();
		return thisDate.compareTo(otherDate) < 0;
	}

	@Override
	public SkypeChatMessage createCopyWithNewTime(Date newMessageTime) {
		return new SkypeChatMessageData(digestProvider, userId, userDisplay, message, newMessageTime);
	}
}
