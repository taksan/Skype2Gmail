package skypeapi.wrappers;

import java.util.Date;

import skype.commons.UserWrapper;


public interface ChatWrapper {

	ChatMessageWrapper[] getAllChatMessages();

	Date getTime();

	String getWindowTitle();

	UserWrapper[] getAllMembers();

	String getId();

}
