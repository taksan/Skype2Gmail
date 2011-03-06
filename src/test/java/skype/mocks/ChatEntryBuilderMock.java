package skype.mocks;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import skype.ChatEntryBuilder;
import skype.SkypeChat;
import skype.SkypeChatMessage;

public class ChatEntryBuilderMock implements ChatEntryBuilder {

	private final SkypeChat chat;

	public ChatEntryBuilderMock(SkypeChat chat) {
		this.chat = chat;
	}

	@Override
	public String getContent() {
		return chat.toString();
	}

	@Override
	public SkypeChatMessage getMostRecentMessage() {
		List<SkypeChatMessage> chatMessages = chat.getChatMessages();
		
		return chatMessages.get(chatMessages.size()-1);
	}

}
