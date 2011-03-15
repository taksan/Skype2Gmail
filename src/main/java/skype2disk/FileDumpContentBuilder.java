package skype2disk;

import java.util.Date;

import skype.ChatContentBuilder;
import skype.SkypeChat;
import skype.SkypeChatMessage;
import skype.SkypeUser;
import skype.TimeSortedMessages;
import skype.UsersSortedByUserId;

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

		appendChatIdAndDate(chat, messageText);
		appendChatBodySignature(chat, messageText);
		appendMessagesIds(messageText);
		appendChatTopic(chat, messageText);
		appendChatMembers(chat, messageText);


		String previousSender = "";
		for (SkypeChatMessage aChatMessage : chatMessages) {
			boolean printSender = shouldPrintSender(previousSender,
					aChatMessage);

			previousSender = aChatMessage.getSenderId();
			messageText.append(aChatMessage.messageText(printSender));
		}

		return messageText.toString().trim();
	}

	private void appendMessagesIds(final StringBuilder messageText) {
		int messageCount = chatMessages.size();
		messageText.append("Messages signatures: [");
		for (SkypeChatMessage aChatMessage : chatMessages) {
			messageText.append(aChatMessage.getId());
			messageCount--;
			if (messageCount > 0) {
				messageText.append(",");
			}
		}
		messageText.append("]\n");
	}
	

	@Override
	public Date getLastModificationTime() {
		return chatMessages.last().getTime();
	}

	private void appendChatTopic(SkypeChat chat, StringBuilder messageText) {
		messageText.append("Chat topic: " + chat.getTopic() + "\n");
	}

	private void appendChatBodySignature(SkypeChat chat,
			StringBuilder messageText) {
		messageText.append("Chat Body Signature: " + chat.getBodySignature() + "\n");
	}
	
	private void appendChatIdAndDate(SkypeChat chat, StringBuilder messageText) {
		String formattedTime = SkypeChatMessage.chatDateFormat.format(chat
				.getTime());
		messageText.append(String.format("Chat Id: %s\n", chat.getId()));
		messageText.append(String.format("Chat Time: %s\n", formattedTime));
	}

	private void appendChatMembers(SkypeChat chat, StringBuilder messageText) {
		UsersSortedByUserId memberIds = chat.getMembersIds();
		
		for (SkypeUser skypeUser : memberIds) {
			messageText.append(
					String.format(
						"Poster: id=%s; display=%s\n", 
						skypeUser.getUserId(),
						skypeUser.getDisplayName()
					)
				);
		}
	}

	private boolean shouldPrintSender(String previousSender,
			SkypeChatMessage aChatMessage) {
		boolean printSender;
		if (previousSender.equals(aChatMessage.getSenderId()))
			printSender = false;
		else
			printSender = true;
		return printSender;
	}

}
