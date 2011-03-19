package skype2gmail;

import skype.SkypeChat;
import skype.StorageEntry;

public interface GmailStorageEntryFactory {

	public StorageEntry produce(SkypeChat previousChat);

}
