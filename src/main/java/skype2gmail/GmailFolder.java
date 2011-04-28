package skype2gmail;

import gmail.GmailMessageInterface;
import skype.SkypeChat;

public interface GmailFolder {

	void deleteMessageBasedOnId(String chatId);
	void appendMessage(GmailMessageInterface gmailMessage);
	void close();
	GmailMessageInterface retrieveMessageEntryFor(SkypeChat skypeChat);
}
