package mail;

import javax.mail.Folder;

import com.google.inject.ImplementedBy;

@ImplementedBy(SkypeImapStore.class)
public interface SkypeMailStore {

	Folder getFolder(String folder);

	void close();

}
