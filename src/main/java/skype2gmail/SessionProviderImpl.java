package skype2gmail;

import java.util.Properties;

import javax.mail.Session;

public class SessionProviderImpl implements SessionProvider {
	
	private Session session;

	public SessionProviderImpl() {
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		session = Session.getDefaultInstance(props, null);
	}

	@Override
	public Session getInstance() {
		return session;
	}

}
