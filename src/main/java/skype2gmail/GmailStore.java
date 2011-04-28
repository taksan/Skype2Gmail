package skype2gmail;

import javax.mail.Folder;

import com.google.inject.ImplementedBy;

@ImplementedBy(GmailStoreImpl.class)
public interface GmailStore {

	Folder getFolder(String folder);

	void close();

}
