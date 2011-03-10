package skype.mocks;

import java.util.Date;

import skype.ChatContentBuilder;
import skype.SkypeChat;
import skype.SkypeChatMessage;
import skype.TimeSortedMessages;

public class ChatContentBuilderMock implements ChatContentBuilder {

	private final SkypeChat chat;

	public ChatContentBuilderMock(SkypeChat chat) {
		this.chat = chat;
	}

	@Override
	public String getContent() {
		return chat.toString();
	}


	@Override
	public Date getLastModificationTime() {
		return getMostRecentMessage().getTime();
	}
	
	private SkypeChatMessage getMostRecentMessage() {
		TimeSortedMessages chatMessages = chat.getChatMessages();
		
		return chatMessages.last();
	}

}
