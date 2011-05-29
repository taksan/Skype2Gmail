package mail;


import java.util.LinkedHashMap;
import java.util.Map;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.search.HeaderTerm;
import javax.mail.search.SearchTerm;

import skype.commons.SkypeChat;
import skype.exceptions.ApplicationException;
import skype.exceptions.MessageProcessingException;
import skype2gmail.SkypeChatFolderProvider;

import com.google.inject.Inject;

public class SkypeMailFolderImpl implements SkypeMailFolder {

	private final SkypeChatFolderProvider chatFolderProvider;
	private Folder skypeChatFolder;
	private final Map<String, SkypeMailMessage> mailMessages = new LinkedHashMap<String, SkypeMailMessage>();
	private final SkypeMailStore mailStore;
	private final SkypeMailMessageFactory mailMessageFactory;

	@Inject
	public SkypeMailFolderImpl(SkypeChatFolderProvider chatFolderProvider,
			SkypeMailStore mailStore,
			SkypeMailMessageFactory mailMessageFactory) {
		this.chatFolderProvider = chatFolderProvider;
		this.mailStore = mailStore;
		this.mailMessageFactory = mailMessageFactory;
	}

	@Override
	public void appendMessage(SkypeMailMessage gmailMessage) {
		Folder rootFolder = getSkypeChatFolderEnsuringItIsInitialized();
		Message[] msgs = new javax.mail.Message[] { 
				gmailMessage.getMimeMessage() 
		};
		try {
			rootFolder.appendMessages(msgs);
		} catch (MessagingException e) {
			throw new ApplicationException(e);
		}
	}
	
	@Override
	public SkypeMailMessage retrieveMessageEntryFor(SkypeChat skypeChat) {
		SearchTerm st = new HeaderTerm(SkypeMailMessage.X_MESSAGE_ID, skypeChat.getId());
		SkypeMailMessage gmailMessage = retrieveSingleMessageMatchingSearchTerm(st);
		
		if (gmailMessage.getBody() == null)
			return null;
		
		mailMessages.put(skypeChat.getId(), gmailMessage);
		return gmailMessage;
	}

	@Override
	public void deleteMessageBasedOnId(String chatId) {
		
		SkypeMailMessage mailMessage = mailMessages.get(chatId);
		if (mailMessage == null) {
			SearchTerm st = new HeaderTerm(SkypeMailMessage.X_MESSAGE_ID, chatId);
			mailMessage = retrieveSingleMessageMatchingSearchTerm(st);
			if (mailMessage == null)
				return;
		}

		mailMessage.delete();
	}

	@Override
	public SkypeMailMessage retrieveSingleMessageMatchingSearchTerm(SearchTerm st) {
		SkypeMailMessage gmailMessage;
		Message[] foundMessages = retrieveAllMessagesForSearchTerm(st);
		ensureNoDuplicatesExists(st, foundMessages);
		gmailMessage = factoryMessageOrEmptyIfNoneExists(foundMessages);
		return gmailMessage;
	}

	private void ensureNoDuplicatesExists(SearchTerm st, Message[] foundMessages) {
		if (foundMessages.length > 1) {
			throw new ApplicationException("Found duplicate message, this is a bug! Search Term: " + st);
		}
	}

	private SkypeMailMessage factoryMessageOrEmptyIfNoneExists(
			Message[] foundMessages) {
		if (foundMessages.length == 0)
			return EmptySkypeMailMessage.create();
		
		return mailMessageFactory.factory((MimeMessage) foundMessages[0]);
	}

	private Message[] retrieveAllMessagesForSearchTerm(SearchTerm st) {
		Folder folder = getSkypeChatFolderEnsuringItIsInitialized();
		Message[] foundMessages;
		try {
			foundMessages = folder.search(st);
		} catch (MessagingException e) {
			throw new MessageProcessingException(e);
		}
		return foundMessages;
	}

	@Override
	public void replaceMessageMatchingTerm(SearchTerm chatIndexSearchTerm,
			SkypeMailMessage replacementMessage) {
		SkypeMailMessage messageToReplace = retrieveSingleMessageMatchingSearchTerm(chatIndexSearchTerm);
		messageToReplace.delete();
		this.appendMessage(replacementMessage);
	}

	@Override
	public void close() {
		try {
			closeSkypeFolderIfItWasOpened();
		} finally {
			mailStore.close();
		}
	}

	private void closeSkypeFolderIfItWasOpened() {
		try {
			if (skypeChatFolder != null) {
				closeFolderAndExpungeDeletedMessages();
			}
		} catch (MessagingException e) {
			throw new ApplicationException(e);
		}
	}

	private void closeFolderAndExpungeDeletedMessages()
			throws MessagingException {
		boolean deleteFlaggedMessages = true;
		skypeChatFolder.close(deleteFlaggedMessages);
	}

	private Folder getSkypeChatFolderEnsuringItIsInitialized() {
		if (skypeChatFolder != null)
			return skypeChatFolder;

		skypeChatFolder = mailStore.getFolder(chatFolderProvider.getFolderName());
		return skypeChatFolder;
	}
}