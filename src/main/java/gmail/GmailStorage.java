package gmail;

import org.apache.commons.lang.NotImplementedException;

import skype.SkypeChat;
import skype.SkypeStorage;
import skype.StorageEntry;

public class GmailStorage implements SkypeStorage {

	@Override
	public StorageEntry newEntry(SkypeChat chat) {
		throw new NotImplementedException();
	}

}
