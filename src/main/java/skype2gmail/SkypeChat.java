package skype2gmail;

import java.util.List;

public interface SkypeChat {

	List<? extends SkypeChatMessage> getChatMessages();

}
