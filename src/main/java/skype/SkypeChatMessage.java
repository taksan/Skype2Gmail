package skype;

import java.text.SimpleDateFormat;
import java.util.Date;

public interface SkypeChatMessage {
	public static final SimpleDateFormat chatDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	public static final SimpleDateFormat chatMessageDateFormat = new SimpleDateFormat("[yyyy/MM/dd HH:mm:ss]");
	
	String getSenderId();

	String getSenderDisplayname();

	String getMessageBody();

	String getSignature();

	Date getTime();

	String messageText(boolean printUser);
}
