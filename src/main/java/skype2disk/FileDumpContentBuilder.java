package skype2disk;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import skype.ChatContentBuilder;
import skype.SkypeChat;
import skype.SkypeChatMessage;
import skype.TimeSortedMessages;

public class FileDumpContentBuilder implements ChatContentBuilder {

	private final SkypeChat chat;
	private TimeSortedMessages chatMessages;

	public FileDumpContentBuilder(SkypeChat chat) {
		this.chat = chat;

		chatMessages = this.chat.getChatMessages();
	}

	@Override
	public String getContent() {
		final StringBuilder messageText = new StringBuilder();

		appendChatTopic(chat, messageText);
		appendChatDate(chat, messageText);
		appendChatMembers(chat, messageText);

		int messageCount = chatMessages.size();
		messageText.append("Messages Ids: [");
		for (SkypeChatMessage aChatMessage : chatMessages) {
			messageText.append(aChatMessage.getId());
			messageCount--;
			if (messageCount > 0) {
				messageText.append(",");
			}
		}
		messageText.append("]\n");

		String previousSender = "";
		for (SkypeChatMessage aChatMessage : chatMessages) {
			boolean printSender = shouldPrintSender(previousSender,
					aChatMessage);

			previousSender = aChatMessage.getUserId();
			messageText.append(aChatMessage.messageText(printSender));
		}

		return messageText.toString().trim();
	}
	

	@Override
	public Date getLastModificationTime() {
		return getMostRecentMessage().getTime();
	}

	private SkypeChatMessage getMostRecentMessage() {
		return chatMessages.last();
	}

	private void appendChatTopic(SkypeChat chat, StringBuilder messageText) {
		messageText.append("Chat Content Code: " + chat.getChatContentId() + "\n");
		messageText.append("Chat topic: " + chat.getTopic() + "\n");
	}

	private void appendChatMembers(SkypeChat chat, StringBuilder messageText) {
		messageText.append("Chat members: [");
		List<String> memberIds = chat.getMembersIds();

		messageText.append(StringUtils.join(memberIds, ","));
		messageText.append("]\n");
	}

	private void appendChatDate(SkypeChat chat, StringBuilder messageText) {
		String formattedTime = SkypeChatMessage.dateFormat.format(chat
				.getTime());
		messageText.append(String.format("Chat [%s] at %s\n", chat.getId(),
				formattedTime));
	}

	private boolean shouldPrintSender(String previousSender,
			SkypeChatMessage aChatMessage) {
		boolean printSender;
		if (previousSender.equals(aChatMessage.getUserId()))
			printSender = false;
		else
			printSender = true;
		return printSender;
	}

}
