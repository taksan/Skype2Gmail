package skype;

import java.text.SimpleDateFormat;
import java.util.Date;

public interface SkypeChatMessage {
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	String getDisplayUsername();

	String getMessageBody();

	String getId();

	Date getTime();

	String getUserId();

	String messageText(boolean printUser);
}
