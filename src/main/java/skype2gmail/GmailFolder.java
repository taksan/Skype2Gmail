package skype2gmail;

import gmail.GmailMessage;
import skype.SkypeChat;

public interface GmailFolder {

	void deleteMessageBasedOnId(String chatId);
	void appendMessage(GmailMessage gmailMessage);
	void close();
	GmailMessage retrieveMessageEntryFor(SkypeChat skypeChat);
	String retrieveIndexFromMail();
}
