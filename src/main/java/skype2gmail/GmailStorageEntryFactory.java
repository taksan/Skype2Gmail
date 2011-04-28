package skype2gmail;

import gmail.GmailMessageInterface;
import skype.SkypeChat;
import skype.StorageEntry;

public interface GmailStorageEntryFactory {

	public StorageEntry produce(SkypeChat newChat, GmailMessageInterface previousChatMessage);

	public StorageEntry produce(SkypeChat newChat);

}
