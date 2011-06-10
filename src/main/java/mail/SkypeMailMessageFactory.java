package mail;

import javax.mail.internet.MimeMessage;

import com.google.inject.ImplementedBy;

@ImplementedBy(SkypeMailMessageFactoryImpl.class)
public interface SkypeMailMessageFactory {
	SkypeMailMessage factory();

	SkypeMailMessage factory(MimeMessage mimeMessage);
}
