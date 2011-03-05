package skype.mocks;

import skype.SkypeChat;
import skype.SkypeStorage;
import skype.StorageEntry;

public class SkypeStorageMock implements SkypeStorage {
	final StringBuilder result = new StringBuilder();

	@Override
	public StorageEntry newEntry(SkypeChat chat) {
		return new StorageEntryMock(this, chat);
	}

	public void addEntryResult(String result) {
		this.result.append(result);
	}
	
	@Override
	public String toString() {
		return "@SkypeStorageMock:\n" + this.result.toString();
	}

}
