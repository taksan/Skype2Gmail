package skype;

import java.util.Date;

import com.skype.ChatMessage;

public interface SkypeChatMessageFactory {
	public SkypeChatMessageData produce(ChatMessage chatMessage);
	public SkypeChatMessageData produce(String userId, String userDisplay, String message, Date time);
}
