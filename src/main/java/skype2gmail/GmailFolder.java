package skype2gmail;

import gmail.GmailMessage;

import javax.mail.search.SearchTerm;

import skype.SkypeChat;

public interface GmailFolder {

	void deleteMessageBasedOnId(String chatId);
	void appendMessage(GmailMessage gmailMessage);
	void close();
	GmailMessage retrieveMessageEntryFor(SkypeChat skypeChat);
	GmailMessage retrieveFirstMessageMatchingSearchTerm(SearchTerm st);
	void replaceMessageMatchingTerm(SearchTerm chatIndexSearchTerm, GmailMessage replacementMessage);
}
