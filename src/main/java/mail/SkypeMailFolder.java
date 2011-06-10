package mail;


import javax.mail.search.SearchTerm;

import com.google.inject.ImplementedBy;

import skype.commons.SkypeChat;
import skype2gmail.IndexedSkypeMailFolder;

@ImplementedBy(IndexedSkypeMailFolder.class)
public interface SkypeMailFolder {

	void deleteMessageBasedOnId(String chatId);
	void appendMessage(SkypeMailMessage gmailMessage);
	void close();
	SkypeMailMessage retrieveMessageEntryFor(SkypeChat skypeChat);
	SkypeMailMessage retrieveSingleMessageMatchingSearchTerm(SearchTerm st);
	void replaceMessageMatchingTerm(SearchTerm chatIndexSearchTerm, SkypeMailMessage replacementMessage);
}
