package skype;

import java.util.Date;
import java.util.List;

public interface SkypeChat {

	List<SkypeChatMessage> getChatMessages();
	
	List<String> getMembersIds();

	String getId();

	Date getTime();

	String getTopic();
}
