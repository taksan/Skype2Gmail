package skype.commons;

import java.util.Date;

import skypeapi.wrappers.ChatMessageWrapper;

public interface SkypeChatMessageFactory {
	public SkypeChatMessageData produce(ChatMessageWrapper chatMessage);
	public SkypeChatMessageData produce(String userId, String userDisplay, String message, Date time);
}
