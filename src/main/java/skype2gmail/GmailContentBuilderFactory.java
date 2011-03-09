package skype2gmail;

import skype.ChatContentBuilder;
import skype.ChatContentBuilderFactory;
import skype.SkypeChat;

public class GmailContentBuilderFactory implements ChatContentBuilderFactory {

	@Override
	public ChatContentBuilder produce(SkypeChat chat) {
		return new GmailContentBuilder(chat);
	}

}
