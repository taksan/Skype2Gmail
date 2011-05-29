package skype2disk;

import skype.commons.SkypeChat;

public interface FileDumpContentParser {

	public abstract SkypeChat parse(String fileContents);

}