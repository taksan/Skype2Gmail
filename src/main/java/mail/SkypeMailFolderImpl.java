package mail;


import java.util.LinkedHashMap;
import java.util.Map;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.search.HeaderTerm;
import javax.mail.search.SearchTerm;

import skype.ApplicationException;
import skype.MessageProcessingException;
import skype.SkypeChat;
import skype2gmail.SkypeChatFolderProvider;

import com.google.inject.Inject;

public class SkypeMailFolderImpl implements SkypeMailFolder {

	private final SkypeChatFolderProvider chatFolderProvider;
	private Folder skypeChatFolder;
	private final Map<String, SkypeMailMessage> gmailMessages = new LinkedHashMap<String, SkypeMailMessage>();
	private final SkypeMailStore gmailStore;
	private final SkypeMailMessageFactory gmailMessageFactory;

	@Inject
	public SkypeMailFolderImpl(SkypeChatFolderProvider chatFolderProvider,
			SkypeMailStore gmailStore,
			SkypeMailMessageFactory gmailMessageFactory) {
		this.chatFolderProvider = chatFolderProvider;
		this.gmailStore = gmailStore;
		this.gmailMessageFactory = gmailMessageFactory;
	}

	@Override
	public void appendMessage(SkypeMailMessage gmailMessage) {
		Folder rootFolder = getSkypeChatFolder();
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
		
		gmailMessages.put(skypeChat.getId(), gmailMessage);
		return gmailMessage;
	}

	@Override
	public void deleteMessageBasedOnId(String chatId) {
		
		SkypeMailMessage gmailMessage = gmailMessages.get(chatId);
		if (gmailMessage == null) {
			SearchTerm st = new HeaderTerm(SkypeMailMessage.X_MESSAGE_ID, chatId);
			gmailMessage = retrieveSingleMessageMatchingSearchTerm(st);
			if (gmailMessage == null)
				return;
		}

		gmailMessage.delete();
	}

	@Override
	public SkypeMailMessage retrieveSingleMessageMatchingSearchTerm(SearchTerm st) {
		SkypeMailMessage gmailMessage;
		Message[] foundMessages = retrieveAllMessagesForSearchTerm(st);
		if (foundMessages.length > 1) {
			throw new ApplicationException("Found duplicate message, this is a bug! Search Term: " + st);
		}
		if (foundMessages.length == 0)
			gmailMessage = new EmptySkypeMailMessage();
		else
			gmailMessage = gmailMessageFactory.factory((MimeMessage) foundMessages[0]);
		return gmailMessage;
	}

	private Message[] retrieveAllMessagesForSearchTerm(SearchTerm st) {
		Folder folder = getSkypeChatFolder();
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

		skypeChatFolder = gmailStore.getFolder(chatFolderProvider.getFolderName());
		return skypeChatFolder;
	}
}
