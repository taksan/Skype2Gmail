package skype;

import java.util.Date;

public interface SkypeChat {

	TimeSortedMessages getChatMessages();
	
	UsersSortedByUserId getMembersIds();

	String getId();

	Date getTime();

	String getTopic();

	String getBodySignature();

	Date getLastModificationTime();

	SkypeChat merge(SkypeChat skypeChat);
}
