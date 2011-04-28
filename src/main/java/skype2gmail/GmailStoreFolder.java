package skype2gmail;

import gmail.GmailFolder;

public interface GmailStoreFolder {

	GmailFolder getFolder();
	void close();
}
