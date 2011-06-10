package skype2gmail;

import java.util.Properties;

import javax.mail.Session;

import com.google.inject.Singleton;

import skype2gmail.api.MailSessionImpl;

import mail.MailSession;

@Singleton
public class SessionProviderImpl implements SessionProvider {
	
	private Session session;

	public SessionProviderImpl() {
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		session = Session.getDefaultInstance(props, null);
	}

	@Override
	public MailSession getInstance() {
		return new MailSessionImpl(session);
	}

}
