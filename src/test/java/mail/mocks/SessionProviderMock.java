package mail.mocks;

import mail.MailSession;
import skype2gmail.SessionProvider;

public class SessionProviderMock implements SessionProvider {
	final SessionMock sessionMock = new SessionMock();

	@Override
	public MailSession getInstance() {
		return sessionMock;
	}

	public MailStoreMock getMockStore() {
		return (MailStoreMock) sessionMock.getStore(null);
	}

}
