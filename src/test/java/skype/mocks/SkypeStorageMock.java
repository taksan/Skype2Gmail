package skype.mocks;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import skype.commons.EmptySkypeChat;
import skype.commons.SkypeChat;
import skype.commons.SkypeStorage;
import skype.commons.StorageEntry;
import utils.SimpleLoggerProvider;

public class SkypeStorageMock implements SkypeStorage {
	final List<StorageEntryMock> recordedChats = new LinkedList<StorageEntryMock>();
	private final String syncId;
	
	public SkypeStorageMock(String syncId) {
		this.syncId = syncId;
	}

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
		return new StorageEntryMock(new EmptySkypeChat(new SimpleLoggerProvider()));
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
	public void open() {
		// nothing to do here
	}
	
	@Override
	public void close() {
		// nothing to do here
	}

	@Override
	public String getSyncId() {
		return syncId;
	}

}
