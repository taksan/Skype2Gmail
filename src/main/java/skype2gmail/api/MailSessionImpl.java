package skype2gmail.api;

import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;

import mail.MailSession;
import mail.MailStore;
import skype.exceptions.ApplicationException;

public class MailSessionImpl implements MailSession {

	private final Session session;

	public MailSessionImpl(Session session) {
		this.session = session;
		
	}

	@Override
	public MailStore getStore(String storeName) {
		try {
			Store store = this.session.getStore(storeName);
			return new MailStoreImpl(store);
		} catch (NoSuchProviderException e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public MimeMessage createMimeMessage() {
		return new MimeMessage(this.session);
	}

}
