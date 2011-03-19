package gmail;

public interface GmailFolder {

	void appendMessage(GmailMessage gmailMessage);

	GmailMessage[] getMessages();

}
