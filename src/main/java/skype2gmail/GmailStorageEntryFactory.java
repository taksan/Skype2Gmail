package skype2gmail;

import gmail.GmailMessage;
import skype.SkypeChat;
import skype.StorageEntry;

public interface GmailStorageEntryFactory {

	public StorageEntry produce(SkypeChat newChat, GmailMessage previousChatMessage);

	public StorageEntry produce(SkypeChat newChat);

}
