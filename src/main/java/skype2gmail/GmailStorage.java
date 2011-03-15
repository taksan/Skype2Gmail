package skype2gmail;

import org.apache.commons.lang.NotImplementedException;

import skype.SkypeChat;
import skype.SkypeStorage;
import skype.StorageEntry;

public class GmailStorage implements SkypeStorage {

	@Override
	public StorageEntry newEntry(SkypeChat chat) {
		return new GmailStorageEntry(chat);
	}

	@Override
	public StorageEntry retrievePreviousEntryFor(SkypeChat skypeChat) {
		throw new NotImplementedException();
	}

}
