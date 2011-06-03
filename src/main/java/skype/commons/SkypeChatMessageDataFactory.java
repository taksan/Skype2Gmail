package skype.commons;

import java.util.Date;

import skypeapi.wrappers.ChatMessageWrapper;
import utils.DigestProvider;

import com.google.inject.Inject;

public class SkypeChatMessageDataFactory implements SkypeChatMessageFactory {

	private final DigestProvider digestProvider;
	
	@Inject
	public SkypeChatMessageDataFactory(DigestProvider digestProvider) {
		this.digestProvider = digestProvider;
		
	}

	@Override
	public SkypeChatMessageData produce(ChatMessageWrapper chatMessage) {
		return new SkypeChatMessageData(digestProvider, chatMessage);
	}

	@Override
	public SkypeChatMessageData produce(String userId, String userDisplay,
			String message, Date time) {
		return new SkypeChatMessageData(digestProvider, userId, userDisplay, message, time);
	}

}
