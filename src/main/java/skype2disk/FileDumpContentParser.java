package skype2disk;

import skype.SkypeChat;

public interface FileDumpContentParser {

	public abstract SkypeChat parse(String fileContents);

}