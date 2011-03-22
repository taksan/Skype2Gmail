package skype.mocks;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import skype.EmptySkypeChat;
import skype.SkypeChat;
import skype.SkypeStorage;
import skype.StorageEntry;

public class SkypeStorageMock implements SkypeStorage {
	final List<StorageEntryMock> recordedChats = new LinkedList<StorageEntryMock>();
	
	@Override
	public StorageEntry newEntry(SkypeChat chat) {
		final StorageEntryMock storageEntryMock = new StorageEntryMock(chat);
		recordedChats.add(storageEntryMock);
		return storageEntryMock;
	}

	@Override
	public StorageEntry retrievePreviousEntryFor(SkypeChat skypeChat) {
		for (StorageEntryMock storageEntry : recordedChats) {
			if (storageEntry.getChat().getId().equals(skypeChat.getId())) {
				return storageEntry;
			}
		}
		return new StorageEntryMock(new EmptySkypeChat());
	}
	
	@Override
	public String toString() {
		final StringBuilder result = new StringBuilder();
		Collections.sort(recordedChats);
		
		for (StorageEntry storageEntry : recordedChats) {
			result.append(storageEntry.toString());
		}
		
		return "@SkypeStorageMock:\n" + result.toString();
	}

	@Override
	public void close() {
		// nothing to do here
	}
}
