package skype;


public interface SkypeStorage {

	StorageEntry newEntry(SkypeChat chat);

	StorageEntry retrievePreviousEntryFor(SkypeChat skypeChat);

	void close();

	void open();

	String getSyncId();
}
