package mail.skypemail;


import javax.mail.internet.MimeMessage;

import mail.MailSession;
import mail.SkypeMailMessage;
import mail.SkypeMailStore;

import org.apache.log4j.Logger;

import skype2gmail.SessionProvider;
import skype2gmail.SkypeChatFolderProvider;
import utils.LoggerProvider;

import com.google.inject.Inject;

public class SkypeMailMessageFactoryImpl implements SkypeMailMessageFactory {

	private final MailSession session;
	private final SkypeChatFolderProvider chatFolderProvider;
	private final SkypeMailStore mailStore;
	private final Logger logger;
	
	@Inject
	public SkypeMailMessageFactoryImpl(SessionProvider sessionProvider, 
			SkypeChatFolderProvider chatFolderProvider,
			SkypeMailStore mailStore,
			LoggerProvider loggerProvider) {
		this.chatFolderProvider = chatFolderProvider;
		this.mailStore = mailStore;
		this.logger = loggerProvider.getPriorityLogger(getClass());
		this.session = sessionProvider.getInstance();
	}

	@Override
	public SkypeMailMessage factory() {
		return new SkypeMailMessageImpl(session, chatFolderProvider, mailStore, logger);
	}
	

	@Override
	public SkypeMailMessage factory(MimeMessage mimeMessage) {
		return new SkypeMailMessageImpl(mimeMessage, chatFolderProvider, mailStore, logger);
	}
}
