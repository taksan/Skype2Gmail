package skype;

public interface SkypeChatHistoryIndex {

	boolean containsMessageByContentId(String chatContentId);

	void addMessageToIndex(SkypeChat chatToWrite);

	void save();

}
