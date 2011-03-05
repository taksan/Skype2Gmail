package skype.mocks;

import skype.ChatEntryBuilder;
import skype.ChatEntryBuilderFactory;
import skype.SkypeChat;

public class ChatEntryBuilderFactoryMock implements ChatEntryBuilderFactory {

	@Override
	public ChatEntryBuilder produce(SkypeChat chat) {
		return new ChatEntryBuilderMock(chat);
	}

}
