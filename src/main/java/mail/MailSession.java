package mail;

import javax.mail.internet.MimeMessage;


public interface MailSession {

	MailStore getStore(String storeName);

	MimeMessage createMimeMessage();

}
