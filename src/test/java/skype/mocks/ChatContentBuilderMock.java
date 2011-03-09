package skype.mocks;

import java.util.Date;
import java.util.List;

import skype.ChatContentBuilder;
import skype.SkypeChat;
import skype.SkypeChatMessage;

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
		List<SkypeChatMessage> chatMessages = chat.getChatMessages();
		
		return chatMessages.get(chatMessages.size()-1);
	}

}
