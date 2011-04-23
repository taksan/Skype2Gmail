package skype;

import java.util.Date;

import utils.DigestProvider;

import com.google.inject.Inject;
import com.skype.ChatMessage;
import com.skype.SkypeException;

public class SkypeChatMessageDataFactory implements SkypeChatMessageFactory {

	private final DigestProvider digestProvider;
	
	@Inject
	public SkypeChatMessageDataFactory(DigestProvider digestProvider) {
		this.digestProvider = digestProvider;
		
	}

	@Override
	public SkypeChatMessageData produce(ChatMessage chatMessage) {
		try {
			return new SkypeChatMessageData(digestProvider, chatMessage);
		} catch (SkypeException e) {
			throw new MessageProcessingException(e);
		}
	}

	@Override
	public SkypeChatMessageData produce(String userId, String userDisplay,
			String message, Date time) {
		return new SkypeChatMessageData(digestProvider, userId, userDisplay, message, time);
	}

}
