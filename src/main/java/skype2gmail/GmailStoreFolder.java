package skype2gmail;

import gmail.GmailMessage;

public interface GmailStoreFolder {

	GmailMessage[] getMessages();
	void deleteMessageBasedOnId(String chatId);
	void appendMessage(GmailMessage gmailMessage);
	void close();
}
