package skype2gmail;

import skype.SkypeChat;

public interface FolderIndex {

	String getSignatureFor(String id);

	void addIndexFor(SkypeChat skypeChat);

	void save();
}
