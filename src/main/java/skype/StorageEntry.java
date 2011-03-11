package skype;

import java.util.Date;

public interface StorageEntry {
	void write(SkypeChat chat);

	void save();

	void setLastModificationTime(Date time);
}
