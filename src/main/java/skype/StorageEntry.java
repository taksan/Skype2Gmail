package skype;

import java.util.Date;

public interface StorageEntry {
	void store(SkypeChat chat);

	void save();

	void setLastModificationTime(Date time);

	SkypeChat getChat();
}
