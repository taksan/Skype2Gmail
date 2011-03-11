package skype2gmail;

import java.util.Date;

import org.apache.commons.lang.NotImplementedException;

import skype.SkypeChat;
import skype.StorageEntry;

public class GmailStorageEntry implements StorageEntry {

	public GmailStorageEntry(SkypeChat chat) {
		throw new NotImplementedException();
	}

	@Override
	public void write(SkypeChat content) {
		throw new NotImplementedException();
	}

	@Override
	public void save() {
		throw new NotImplementedException();
	}

	@Override
	public void setLastModificationTime(Date time) {
		throw new NotImplementedException();
	}

}
