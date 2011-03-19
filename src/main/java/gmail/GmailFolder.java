package gmail;

import javax.mail.Message;


public interface GmailFolder {

	void appendMessage(GmailMessage gmailMessage);

	Message[] getMessages();

}
