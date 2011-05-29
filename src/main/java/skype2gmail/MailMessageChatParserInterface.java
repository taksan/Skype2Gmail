package skype2gmail;

import mail.SkypeMailMessage;
import skype.SkypeChat;

import com.google.inject.ImplementedBy;

@ImplementedBy(MailMessageChatParser.class)
public interface MailMessageChatParserInterface {
	public SkypeChat parse(SkypeMailMessage message);
}
