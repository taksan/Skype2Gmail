package skype.mocks;

import skype.ChatEntryBuilder;
import skype.SkypeChat;

public class ChatEntryBuilderMock implements ChatEntryBuilder {

	private final SkypeChat chat;

	public ChatEntryBuilderMock(SkypeChat chat) {
		this.chat = chat;
	}

	@Override
	public String getContent() {
		return chat.toString();
	}

}
