package skype;

import java.util.Date;

public interface StorageEntry {
	void write(String content);

	void save();

	void setLastModificationTime(Date time);
}
