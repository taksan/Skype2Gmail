package skype2disk;

import skype.ChatEntryBuilder;
import skype.ChatEntryBuilderFactory;
import skype.SkypeChat;

public class FileDumpEntryBuilderFactory implements ChatEntryBuilderFactory {

	@Override
	public ChatEntryBuilder produce(SkypeChat chat) {
		return new FileDumpEntryBuilder(chat);
	}

}
