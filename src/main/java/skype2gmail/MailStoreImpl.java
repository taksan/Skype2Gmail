package skype2gmail;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;

import mail.MailStore;
import skype.ApplicationException;

public class MailStoreImpl implements MailStore {

	private final Store store;

	public MailStoreImpl(Store store) {
		this.store = store;
		
	}

	@Override
	public void close() {
		try {
			store.close();
		} catch (MessagingException e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public Folder getFolder(String folderName) {
		try {
			return store.getFolder(folderName);
		} catch (MessagingException e) {
			throw new ApplicationException(e);
		} 
	}

	@Override
	public void connect(String address, String user, String password) {
		try {
			store.connect(address,user,password);
		} catch (MessagingException e) {
			// TODO if connection fails because of authentication, the password should not be saved!
			throw new ApplicationException(e);
		}
	}
}
