package skype.mocks;

import skype.ChatContentBuilder;
import skype.ChatContentBuilderFactory;
import skype.SkypeChat;

public class ChatContentBuilderFactoryMock implements ChatContentBuilderFactory {

	@Override
	public ChatContentBuilder produce(SkypeChat chat) {
		return new ChatContentBuilderMock(chat);
	}

}
