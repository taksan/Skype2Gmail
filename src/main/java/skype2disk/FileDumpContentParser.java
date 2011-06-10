package skype2disk;

import com.google.inject.ImplementedBy;

import skype.commons.SkypeChat;

@ImplementedBy(FileDumpContentParserImpl.class)
public interface FileDumpContentParser {

	public abstract SkypeChat parse(String fileContents);

}