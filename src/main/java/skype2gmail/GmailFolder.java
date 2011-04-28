package skype2gmail;

import gmail.GmailMessage;

public interface GmailFolder {

	GmailMessage[] getMessages();
	void deleteMessageBasedOnId(String chatId);
	void appendMessage(GmailMessage gmailMessage);
	void close();
}
