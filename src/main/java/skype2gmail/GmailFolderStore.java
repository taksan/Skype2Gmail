package skype2gmail;

import gmail.GmailFolder;

public interface GmailFolderStore {

	GmailFolder getFolder();
	void close();
}
