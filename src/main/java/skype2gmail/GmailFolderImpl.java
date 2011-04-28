package skype2gmail;

import gmail.GmailFolder;
import gmail.GmailMessage;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

import org.apache.log4j.Logger;

import skype.ApplicationException;
import utils.LoggerProvider;

import com.sun.mail.imap.IMAPMessage;

public class GmailFolderImpl implements GmailFolder {

	private final Folder root;
	private final Map<String, GmailMessage> gmailMessages = new LinkedHashMap<String, GmailMessage>();
	private GmailMessage[] retrievedMessages;
	private Logger logger;

	public GmailFolderImpl(Folder root, LoggerProvider loggerProvider) {
		this.root = root;
		logger = loggerProvider.getLogger(getClass());
	}

	@Override
	public GmailMessage[] getMessages() {
		try {
			if (retrievedMessages != null) {
				return retrievedMessages;
			}
			logger.info("Retrieving mail chat messages to merge with new messages");
			logger.info("Messages to retrieve: " + root.getMessageCount());
			for (Message message : root.getMessages()) {
				GmailMessage  gmailMessage = new GmailMessage((IMAPMessage)message);
				gmailMessages.put(gmailMessage.getChatId(), gmailMessage);
			}
			logger.info("Messages retrieved");
			retrievedMessages = gmailMessages.values().toArray(new GmailMessage[0]);
			return retrievedMessages;
		} catch (MessagingException e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public void appendMessage(GmailMessage gmailMessage) {
		Message[] msgs = new javax.mail.Message[] { gmailMessage.getMimeMessage() };
		try {
			root.appendMessages(msgs);
			replaceOldMessage(gmailMessage);
		} catch (MessagingException e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public void deleteMessageBasedOnId(String chatId) {
		GmailMessage gmailMessage = gmailMessages.get(chatId);
		if (gmailMessage == null)
			return;
		
		gmailMessage.delete();
	}
	

	private void replaceOldMessage(GmailMessage gmailMessage) {
		gmailMessages.put(gmailMessage.getChatId(), gmailMessage);
		retrievedMessages = gmailMessages.values().toArray(new GmailMessage[0]);
	}

	@Override
	public void close() {
		try {
			boolean deleteFlaggedMessages = true;
			root.close(deleteFlaggedMessages);
		} catch (MessagingException e) {
			throw new ApplicationException(e);
		}
	}
}
