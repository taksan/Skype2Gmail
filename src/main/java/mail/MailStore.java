package mail;

import javax.mail.Folder;

public interface MailStore {

	void close();

	Folder getFolder(String folderName);

	void connect(String address, String user, String password);

}
