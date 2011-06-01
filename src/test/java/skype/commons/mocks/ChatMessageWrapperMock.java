package skype.commons.mocks;

import java.util.Date;

import skypeapi.wrappers.ChatMessageWrapper;

import com.skype.SkypeException;

public class ChatMessageWrapperMock implements ChatMessageWrapper {

	private final String senderId;
	private final String senderDisplayName;
	private final String content;
	private Date time;

	public ChatMessageWrapperMock(UserWrapperMock sender, String content,
			Date msgTime) throws SkypeException {
		this.senderId = sender.getId();
		this.senderDisplayName = sender.getFullName();
		this.content = content;
		this.time = msgTime;
	}

	@Override
	public String getSenderId() {
		return senderId;
	}

	@Override
	public String getSenderDisplayName() {
		return senderDisplayName;
	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public Date getTime() {
		return time;
	}

}
