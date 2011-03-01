package skype;

import java.util.Date;
import java.util.List;

public interface SkypeChat {

	List<SkypeChatMessage> getChatMessages();

	String getId();

	Date getTime();

	String getTopic();

	List<String> getMembersIds();

}
