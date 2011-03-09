package skype2disk;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import skype.ChatContentBuilder;
import skype.SkypeChat;
import skype.SkypeChatMessage;

public class FileDumpContentBuilder implements ChatContentBuilder {

	private final SkypeChat chat;
	private List<SkypeChatMessage> chatMessages;

	public FileDumpContentBuilder(SkypeChat chat) {
		this.chat = chat;

		chatMessages = this.chat.getChatMessages();
		sortChatMessagesByTime(chatMessages);
	}

	@Override
	public String getContent() {
		final StringBuilder messageText = new StringBuilder();

		appendChatTopic(chat, messageText);
		appendChatId(chat, messageText);
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
		return chatMessages.get(chatMessages.size()-1);
	}

	private void sortChatMessagesByTime(List<SkypeChatMessage> chatMessages) {
		Collections.sort(chatMessages, new Comparator<SkypeChatMessage>() {
			@Override
			public int compare(SkypeChatMessage o1, SkypeChatMessage o2) {
				return o1.getTime().compareTo(o2.getTime());
			}
		});
	}

	private void appendChatTopic(SkypeChat chat, StringBuilder messageText) {
		messageText.append("Chat topic: " + chat.getTopic() + "\n");
	}

	private void appendChatMembers(SkypeChat chat, StringBuilder messageText) {
		messageText.append("Chat members: [");
		List<String> memberIds = chat.getMembersIds();

		messageText.append(StringUtils.join(memberIds, ","));
		messageText.append("]\n");
	}

	private void appendChatId(SkypeChat chat, StringBuilder messageText) {
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
