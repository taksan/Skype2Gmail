package skype2gmail;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.Store;

import org.apache.commons.lang.NotImplementedException;

import skype.SkypeHistoryRecorder;

public class Skype2Gmail implements SkypeHistoryRecorder {

	private static final String GMAIL_IMAP_ADDRESS = "imap.gmail.com";

	public Skype2Gmail(String username, String password) {
		throw new NotImplementedException();
	}

	@Override
	public void record() {
		throw new NotImplementedException();
	}

	@SuppressWarnings("unused")
	private Store getGmailStorage(String username, String password) {
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		Session session = Session.getDefaultInstance(props, null);
		
		try {
			Store store = session.getStore("imaps");
			store.connect(GMAIL_IMAP_ADDRESS, username, password);
			
			return store;
		}
		catch(Exception ex){
			throw new IllegalStateException(ex);
		}
	}
}
