package skypeapi.wrappers;

import java.util.Date;

public interface ChatMessageWrapper {

	String getSenderId();

	String getSenderDisplayName();

	String getContent();

	Date getTime();

}
