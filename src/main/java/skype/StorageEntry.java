package skype;

import java.util.Date;

public interface StorageEntry {
	void store(SkypeChatSetter chat);

	void save();

	void setLastModificationTime(Date time);

	SkypeChat getChat();
}
