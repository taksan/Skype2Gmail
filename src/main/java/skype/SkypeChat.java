package skype;

import java.util.Date;
import java.util.List;

public interface SkypeChat {

	TimeSortedMessages getChatMessages();
	
	List<String> getMembersIds();

	String getId();

	Date getTime();

	String getTopic();

	String getChatContentId();

	Date getLastModificationTime();
}
