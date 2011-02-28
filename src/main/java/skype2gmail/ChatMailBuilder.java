package skype2gmail;

import java.util.List;


public class ChatMailBuilder {
	private final List<SkypeChatMessage> chatMessages;
	private final StringBuilder messageText;
	private final SkypeChat chat;
	

	public ChatMailBuilder(SkypeChat chat) {
		this.chat = chat;
		this.chatMessages = chat.getChatMessages();
		this.messageText = new StringBuilder();
		
	}
	
	private void append(SkypeChatMessage aChatMessage){
		throw new NotImplementedException();
	}
	
	public String asString() {
		for (SkypeChatMessage aChatMessage : this.chatMessages) {
			this.append(aChatMessage);
		}
		
		return this.messageText.toString();
	}
}
