package mail.mocks;

import javax.mail.internet.MimeMessage;

import mail.MailSession;
import mail.MailStore;

import org.apache.commons.lang.NotImplementedException;

public class SessionMock implements MailSession {
	final MailStoreMock mailStoreMock = new MailStoreMock();

	@Override
	public MailStore getStore(String storeName) {
		return mailStoreMock;
	}

	@Override
	public MimeMessage createMimeMessage() {
		throw new NotImplementedException();
	}

}
