package skype2gmail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.skype.ChatMessage;
import com.skype.SkypeException;

public class SkypeChatMessageData implements SkypeChatMessage {

	
	private final String msgId;
	private final String userDisplay;
	private final String message;
	private final Date date;
	private final String userId;

	
	public SkypeChatMessageData(ChatMessage chatMessage) throws ParseException, SkypeException {
		this(
			chatMessage.getId(),
			chatMessage.getSenderId(),
			chatMessage.getSenderDisplayName(),
			chatMessage.getContent(),
			chatMessage.getTime()
			);
	}
	
	public SkypeChatMessageData(String msgId, String userId,
			String userDisplay, String message, Date time) throws ParseException {
		this.msgId = msgId;
		this.userDisplay = userDisplay;
		this.message = message;
		this.date = time;
		this.userId = userId;
	}


	@Override
	public String getDisplayUsername() {
		return this.userDisplay;
	}

	@Override
	public String getMessageBody() {
		return this.message;
	}

	@Override
	public String getId() {
		return this.msgId;
	}

	@Override
	public Date getDate() {
		return this.date;
	}

	@Override
	public String getUserId() {
		return this.userId;
	}

	@Override
	public String messageText(boolean printSender) {
		String formattedTime = new SimpleDateFormat("H:m:s").format(this.getDate());
		final String senderDisplayName;
		if (printSender)
			senderDisplayName = this.getDisplayUsername()+":";
		else
			senderDisplayName = "...";
		
		return String.format("[%s] %s %s\n",
				formattedTime,
				senderDisplayName,
				this.getMessageBody()
				);
	}

}
