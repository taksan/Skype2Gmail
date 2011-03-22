package gmail;

public interface GmailFolder {

	void appendMessage(GmailMessage gmailMessage);

	GmailMessage[] getMessages();

	void deleteMessageBasedOnId(String chatId);

	void close();

}
