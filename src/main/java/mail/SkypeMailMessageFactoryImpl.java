package mail;


import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import skype.MessageProcessingException;
import skype2gmail.FolderIndex;
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
		return new SkypeMailMessageImpl(session);
	}
	

	@Override
	public SkypeMailMessage factory(MimeMessage mimeMessage) {
		return new SkypeMailMessageImpl(mimeMessage);
	}
	
	private class SkypeMailMessageImpl extends AbstractSkypeMailMessage {

		SkypeMailMessageImpl(MailSession session) {
			super(session);
		}

		public SkypeMailMessageImpl(MimeMessage message) {
			super(message);
		}
		
		@Override
		public void delete() {
			String skypeFolderName = chatFolderProvider.getFolderName();
			Folder skypeFolder = mailStore.getFolder(skypeFolderName);
			Folder trash =  mailStore.getFolder("[Gmail]/Trash");
			MimeMessage mimeMessage = this.getMimeMessage();
			Message messageToRemove = mimeMessage;
			try {
				Message[] messagesToRemove = new Message[]{messageToRemove};
				skypeFolder.copyMessages(messagesToRemove, trash);
			} catch (MessagingException e) {
				logger.error("Could not transfer message to delete: " + this.getTopic());
				throw new MessageProcessingException(e);
			}
			try {
				mimeMessage.setFlag(Flags.Flag.DELETED, true);
				Message[] messages = trash.search(FolderIndex.CHAT_INDEX_SEARCH_TERM);
				if (messages.length>0)
					messages[0].setFlag(Flags.Flag.DELETED, true);
				trash.expunge();
			} catch (MessagingException e) {
				throw new MessageProcessingException(e);
			}
		}
	}
}
