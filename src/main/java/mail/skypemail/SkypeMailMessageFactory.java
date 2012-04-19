package mail.skypemail;

import javax.mail.internet.MimeMessage;

import mail.SkypeMailMessage;

import com.google.inject.ImplementedBy;

@ImplementedBy(SkypeMailMessageFactoryImpl.class)
public interface SkypeMailMessageFactory {
	SkypeMailMessage factory();

	SkypeMailMessage factory(MimeMessage mimeMessage);
}
