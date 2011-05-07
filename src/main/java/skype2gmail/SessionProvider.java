package skype2gmail;

import mail.MailSession;

public interface SessionProvider {

	MailSession getInstance();

}
