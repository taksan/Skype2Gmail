package skype2disk;

import java.util.Date;

import skype.ChatContentBuilder;
import skype.MessageBodyBuilder;
import skype.SkypeChat;
import skype.SkypeChatMessage;
import skype.SkypeChatSetter;
import skype.SkypeUser;
import skype.TimeSortedMessages;

public class FileDumpContentBuilder implements ChatContentBuilder, SkypeChatSetterVisitor {

	public static final String MESSAGE_TIME_FORMAT = "\\[\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}]";
	public static final String MESSAGE_TIME_FORMAT_FOR_PARSING = "\r?\n(?="+MESSAGE_TIME_FORMAT+")";
	private final SkypeChat chat;
	private TimeSortedMessages chatMessages;
	private final StringBuilder messageText = new StringBuilder();
	private MessageBodyBuilder messageBodyBuilder = new MessageBodyBuilder();

	public static String escape(String messageLine) {
		return messageLine.replaceAll("(?m)\n("+MESSAGE_TIME_FORMAT+"|\\\\)", "\n\\\\$1");
	}
	
	public static String unescape(String message) {
		return message.replaceAll("(?m)\n\\\\", "\n");
	}	


	public FileDumpContentBuilder(SkypeChat chat) {
		this.chat = chat;

		chatMessages = this.chat.getChatMessages();
	}
	

	@Override
	public Date getLastModificationTime() {
		return chatMessages.last().getTime();
	}

	@Override
	public String getContent() {
		
		new SkypeChatSetter(chat).accept(this);
		messageText.append(messageBodyBuilder.getMessageBody());

		return messageText.toString();
	}

	// SkypeChatSetterVisitor implementation

	@Override
	public void visitChatAuthor(String chatAuthor) {
	}
	
	@Override
	public void visitChatId(String id) {
		messageText.append(String.format("Chat Id: %s\n", id));
	}

	@Override
	public void visitDate(Date time) {
		String formattedTime = SkypeChatMessage.chatDateFormat.format(time);
		messageText.append(String.format("Chat Time: %s\n", formattedTime));
	}

	@Override
	public void visitBodySignature(String bodySignature) {
		messageText.append("Chat Body Signature: " + bodySignature + "\n");
	}
	
	@Override
	public void visitMessagesSignatures(String signatures) {
		messageText.append("Messages signatures: [");
		messageText.append(signatures);
		messageText.append("]\n");
	}
	
	@Override
	public void visitTopic(String topic) {
		messageText.append("Chat topic: " + topic + "\n");
	}
	
	@Override
	public void visitPoster(SkypeUser skypeUser) {
		messageText.append(skypeUser.getPosterHeader());
		messageText.append("\n");
	}
	 
	@Override
	public void visitMessage(SkypeChatMessage aChatMessage) {	
		messageBodyBuilder.appendMessage(aChatMessage);
	}

	@Override
	public void visitLastModifiedDate(Date time) {
	}

}
