package skype2gmail;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;


public class ChatMailBuilder {
	private final List<SkypeChatMessage> chatMessages;
	private final StringBuilder messageText;

	public ChatMailBuilder(SkypeChat chat) {
		this.chatMessages = chat.getChatMessages();
		this.messageText = new StringBuilder();
		
		appendChatTopic(chat);
		appendChatId(chat);
		appendChatMembers(chat);
	}
	private void appendChatTopic(SkypeChat chat) {
		this.messageText.append("Chat topic: " + chat.getTopic() + "\n");
	}

	private void appendChatMembers(SkypeChat chat) {
		this.messageText.append("Chat members: [");
		List<String> memberIds = chat.getMembersIds();
		
		this.messageText.append(StringUtils.join(memberIds, ","));
		this.messageText.append("]\n");
	}


	private void appendChatId(SkypeChat chat) {
		String formattedTime = SkypeChatMessage.dateFormat.format(chat.getTime());
		this.messageText.append(
				String.format("Chat [%s] at %s\n", chat.getId(), formattedTime)
				);
	}
	
	public String toChatText() {
		Collections.sort(this.chatMessages, new Comparator<SkypeChatMessage>() {

			@Override
			public int compare(SkypeChatMessage o1, SkypeChatMessage o2) {
				return o1.getDate().compareTo(o2.getDate());
			}
		});
		
		int messageCount = this.chatMessages.size();
		this.messageText.append("[");
		for (SkypeChatMessage aChatMessage : this.chatMessages) {
			this.messageText.append(aChatMessage.getId());
			messageCount--;
			if (messageCount > 0) {
				this.messageText.append(",");
			}
		}
		this.messageText.append("]\n");
		
		String previousSender = null;
		for (SkypeChatMessage aChatMessage : this.chatMessages) {
			boolean printSender = shouldPrintSender(previousSender, aChatMessage);
			
			previousSender = aChatMessage.getUserId();
			this.messageText.append(aChatMessage.messageText(printSender));
		}
		
		return this.messageText.toString().trim();
	}

	private boolean shouldPrintSender(String previousSender,
			SkypeChatMessage aChatMessage) {
		boolean printSender;
		if (previousSender == aChatMessage.getUserId())
			printSender = false;
		else
			printSender = true;
		return printSender;
	}
}
