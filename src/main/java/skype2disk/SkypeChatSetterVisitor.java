package skype2disk;

import java.util.Date;

import skype.SkypeChatMessage;
import skype.SkypeUser;

public interface SkypeChatSetterVisitor {

	void visitChatAuthor(String chatAuthor);
	void visitChatId(String id);
	void visitDate(Date time);
	void visitBodySignature(String bodySignature);
	void visitMessagesSignatures(String signatures);
	void visitTopic(String topic);
	void visitPoster(SkypeUser skypeUser);
	void visitMessage(SkypeChatMessage skypeChatMessage);
	void visitLastModifiedDate(Date time);
}
