package skype2gmail;

import java.text.SimpleDateFormat;
import java.util.Date;

public interface SkypeChatMessage {
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd H:m:s");

	String getDisplayUsername();

	String getMessageBody();

	String getId();

	Date getDate();

	String getUserId();

	String messageText(boolean printUser);
}
