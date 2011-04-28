package skype2gmail;

import gmail.GmailMessage;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.search.HeaderTerm;
import javax.mail.search.SearchTerm;

import org.apache.log4j.Logger;

import skype.ApplicationException;
import skype.MessageProcessingException;
import skype.SkypeChat;
import utils.LoggerProvider;

import com.google.inject.Inject;
import com.sun.mail.imap.IMAPMessage;

public class GmailFolderImpl implements GmailFolder {

	private final SkypeChatFolderProvider chatFolderProvider;
	private Folder skypeChatFolder;
	private Logger logger;
	private GmailMessage[] retrievedMessages;
	private final Map<String, GmailMessage> gmailMessages = new LinkedHashMap<String, GmailMessage>();
	private final GmailStore gmailStore;

	@Inject
	public GmailFolderImpl(SkypeChatFolderProvider chatFolderProvider,
			LoggerProvider loggerProvider, GmailStore gmailStore) {
		this.chatFolderProvider = chatFolderProvider;
		this.gmailStore = gmailStore;
		this.logger = loggerProvider.getLogger(getClass());
	}

	public GmailMessage[] searchByChatId() throws MessagingException {
		SearchTerm st = new HeaderTerm("X-SKYPE-MESSAGE-ID",
				"#gab@rielsan/$aroliveira86;c15828e1bcea9030");
		Folder folder = getSkypeChatFolder();
		Message[] search = folder.search(st);
		for (Message message : search) {
			GmailMessage gmailMessage = new GmailMessage((IMAPMessage) message);
			gmailMessages.put(gmailMessage.getChatId(), gmailMessage);
		}
		logger.info("Messages retrieved");
		retrievedMessages = gmailMessages.values().toArray(new GmailMessage[0]);
		return retrievedMessages;
	}

	@Override
	public void deleteMessageBasedOnId(String chatId) {
		GmailMessage gmailMessage = gmailMessages.get(chatId);
		if (gmailMessage == null)
			return;

		gmailMessage.delete();
	}

	@Override
	public void appendMessage(GmailMessage gmailMessage) {
		Folder rootFolder = getSkypeChatFolder();
		Message[] msgs = new javax.mail.Message[] { gmailMessage
				.getMimeMessage() };
		try {
			rootFolder.appendMessages(msgs);
			replaceOldMessage(gmailMessage);
		} catch (MessagingException e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public void close() {
		try {
			try {
				if (skypeChatFolder != null) {
					boolean deleteFlaggedMessages = true;
					skypeChatFolder.close(deleteFlaggedMessages);
				}
			} catch (MessagingException e) {
				throw new ApplicationException(e);
			}
		} finally {
			gmailStore.close();
		}
	}

	private Folder getSkypeChatFolder() {
		if (skypeChatFolder != null)
			return skypeChatFolder;

		return gmailStore.getFolder(chatFolderProvider.getFolder());
	}

	private void replaceOldMessage(GmailMessage gmailMessage) {
		gmailMessages.put(gmailMessage.getChatId(), gmailMessage);
		retrievedMessages = gmailMessages.values().toArray(new GmailMessage[0]);
	}

	@Override
	public GmailMessage retrieveMessageEntryFor(SkypeChat skypeChat) {
		SearchTerm st = new HeaderTerm(GmailMessage.X_MESSAGE_ID,
				skypeChat.getId());
		Folder folder = getSkypeChatFolder();
		Message[] foundMessages;
		try {
			foundMessages = folder.search(st);
		} catch (MessagingException e) {
			throw new MessageProcessingException(e);
		}
		if (foundMessages.length == 0)
			return null;

		return new GmailMessage((IMAPMessage) foundMessages[0]);
	}
}
