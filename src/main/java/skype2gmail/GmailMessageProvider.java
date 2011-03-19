package skype2gmail;

import javax.mail.Message;

public interface GmailMessageProvider {

	Message[] getMessages();

}
