package mail;

import javax.mail.internet.MimeMessage;


public interface SkypeMailMessageFactory {
	SkypeMailMessage factory();

	SkypeMailMessage factory(MimeMessage mimeMessage);
}
