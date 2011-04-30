package skype2gmail;

import gmail.GmailMessage;
import gmail.GmailMessageImpl;

import javax.mail.Session;

import com.google.inject.Inject;

public class GmailMessageFactoryImpl implements GmailMessageFactory {

	private final Session session;
	
	@Inject
	public GmailMessageFactoryImpl(SessionProvider sessionProvider) {
		this.session = sessionProvider.getInstance();
	}

	@Override
	public GmailMessage factory() {
		return new GmailMessageImpl(session);
	}

}
