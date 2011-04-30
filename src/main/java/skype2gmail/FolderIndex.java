package skype2gmail;


import javax.mail.search.HeaderTerm;

import mail.SkypeMailMessage;

import skype.SkypeChat;

public interface FolderIndex {

	public static final String INDEX_HEADER_NAME = SkypeMailMessage.X_SKYPE_2_GMAIL_INDEX;
	public static final String INDEX_HEADER_VALUE = "skype2gmail";
	public static final HeaderTerm CHAT_INDEX_SEARCH_TERM = new HeaderTerm(INDEX_HEADER_NAME, INDEX_HEADER_VALUE);

	String getSignatureFor(String id);

	void addIndexFor(SkypeChat skypeChat);

	void save();
}
