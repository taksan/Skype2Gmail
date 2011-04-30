package skype2gmail;

import skype.ChatContentBuilder;
import skype.ChatContentBuilderFactory;
import skype.SkypeChat;

public class MailContentBuilderFactory implements ChatContentBuilderFactory {

	@Override
	public ChatContentBuilder produce(SkypeChat chat) {
		return new MailContentBuilder(chat);
	}

}
