package skype2gmail;

import gmail.GmailMessage;

public interface GmailStore {

	GmailMessage[] getMessages();
	void deleteMessageBasedOnId(String chatId);
	void appendMessage(GmailMessage gmailMessage);
	void close();
}
