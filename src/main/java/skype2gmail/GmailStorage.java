package skype2gmail;

import skype.SkypeChat;
import skype.SkypeStorage;
import skype.StorageEntry;

public class GmailStorage implements SkypeStorage {

	@Override
	public StorageEntry newEntry(SkypeChat chat) {
		return new GmailStorageEntry(chat);
	}

}
