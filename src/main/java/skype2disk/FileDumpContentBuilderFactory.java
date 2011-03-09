package skype2disk;

import skype.ChatContentBuilder;
import skype.ChatContentBuilderFactory;
import skype.SkypeChat;

public class FileDumpContentBuilderFactory implements ChatContentBuilderFactory {

	@Override
	public ChatContentBuilder produce(SkypeChat chat) {
		return new FileDumpContentBuilder(chat);
	}

}
