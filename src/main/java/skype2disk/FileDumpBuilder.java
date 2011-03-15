package skype2disk;

import org.apache.commons.lang.StringUtils;

import skype.SkypeChat;
import skype.SkypeChatMessage;
import skype.TimeSortedMessages;
import skype.UsersSortedByUserId;


public class FileDumpBuilder {
	private final TimeSortedMessages chatMessages;
	private final SkypeChat chat;

	public FileDumpBuilder(SkypeChat chat) {
		this.chat = chat;
		this.chatMessages = chat.getChatMessages();
	}
	
	public String toChatText() {
		final StringBuilder messageText = new StringBuilder();
		
		appendChatTopic(chat, messageText);
		appendChatId(chat, messageText);
		appendChatMembers(chat, messageText);

		int messageCount = this.chatMessages.size();
		messageText.append("[");
		for (SkypeChatMessage aChatMessage : this.chatMessages) {
			messageText.append(aChatMessage.getId());
			messageCount--;
			if (messageCount > 0) {
				messageText.append(",");
			}
		}
		messageText.append("]\n");
		
		String previousSender = null;
		for (SkypeChatMessage aChatMessage : this.chatMessages) {
			boolean printSender = shouldPrintSender(previousSender, aChatMessage);
			
			previousSender = aChatMessage.getSenderId();
			messageText.append(aChatMessage.messageText(printSender));
		}
		
		return messageText.toString().trim();
	}
	
	private void appendChatTopic(SkypeChat chat, StringBuilder messageText) {
		messageText.append("Chat topic: " + chat.getTopic() + "\n");
	}

	private void appendChatMembers(SkypeChat chat, StringBuilder messageText) {
		messageText.append("Chat members: [");
		UsersSortedByUserId memberIds = chat.getMembersIds();
		
		messageText.append(StringUtils.join(memberIds, ","));
		messageText.append("]\n");
	}


	private void appendChatId(SkypeChat chat, StringBuilder messageText) {
		String formattedTime = SkypeChatMessage.chatDateFormat.format(chat.getTime());
		messageText.append(
				String.format("Chat [%s] at %s\n", chat.getId(), formattedTime)
				);
	}

	private boolean shouldPrintSender(String previousSender,
			SkypeChatMessage aChatMessage) {
		boolean printSender;
		if (previousSender == aChatMessage.getSenderId())
			printSender = false;
		else
			printSender = true;
		return printSender;
	}
}
