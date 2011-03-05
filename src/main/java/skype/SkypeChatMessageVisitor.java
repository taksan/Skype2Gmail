package skype;

import java.util.Date;

import com.skype.User;

public interface SkypeChatMessageVisitor {
	void visit(String chatId, String topic, Date chatStartTime);
	void visit(User chatMember);
	void visit(SkypeChatMessage chatMessage);
}
