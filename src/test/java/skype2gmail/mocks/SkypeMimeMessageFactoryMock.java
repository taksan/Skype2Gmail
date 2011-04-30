package skype2gmail.mocks;


import javax.mail.internet.MimeMessage;

import mail.SkypeMailMessage;
import mail.SkypeMailMessageFactory;

import org.apache.commons.lang.NotImplementedException;

public class SkypeMimeMessageFactoryMock implements SkypeMailMessageFactory {

	@Override
	public SkypeMailMessage factory() {
		return new SkypeMailMessageMock();
	}

	@Override
	public SkypeMailMessage factory(MimeMessage mimeMessage) {
		throw new NotImplementedException();
	}
}
