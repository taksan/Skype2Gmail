package skype2gmail;

import gmail.GmailMessage;

import javax.mail.search.HeaderTerm;

import skype.SkypeChat;

public interface FolderIndex {

	public static final String INDEX_HEADER_NAME = GmailMessage.X_SKYPE_2_GMAIL_INDEX;
	public static final String INDEX_HEADER_VALUE = "skype2gmail";
	public static final HeaderTerm CHAT_INDEX_SEARCH_TERM = new HeaderTerm(INDEX_HEADER_NAME, INDEX_HEADER_VALUE);

	String getSignatureFor(String id);

	void addIndexFor(SkypeChat skypeChat);

	void save();
}
