package skype2gmail;

import com.google.inject.ImplementedBy;

import mail.MailSession;

@ImplementedBy(SessionProviderImpl.class)
public interface SessionProvider {

	MailSession getInstance();

}
