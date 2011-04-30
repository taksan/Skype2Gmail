package skype2gmail;

import gmail.GmailMessage;
import gmail.GmailMessageImpl;

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

import com.google.inject.Inject;

public class GmailFolderImpl implements GmailFolder {

	private final SkypeChatFolderProvider chatFolderProvider;
	private Folder skypeChatFolder;
	private final Map<String, GmailMessage> gmailMessages = new LinkedHashMap<String, GmailMessage>();
	private final GmailStore gmailStore;

	@Inject
	public GmailFolderImpl(SkypeChatFolderProvider chatFolderProvider,
			GmailStore gmailStore) {
		this.chatFolderProvider = chatFolderProvider;
		this.gmailStore = gmailStore;
	}

	@Override
	public void appendMessage(GmailMessage gmailMessage) {
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
	public GmailMessage retrieveMessageEntryFor(SkypeChat skypeChat) {
		SearchTerm st = new HeaderTerm(GmailMessage.X_MESSAGE_ID, skypeChat.getId());
		GmailMessage gmailMessage = retrieveFirstMessageMatchingSearchTerm(st);
		
		if (gmailMessage.getBody() == null)
			return null;
		
		gmailMessages.put(skypeChat.getId(), gmailMessage);
		return gmailMessage;
	}

	@Override
	public void deleteMessageBasedOnId(String chatId) {
		
		GmailMessage gmailMessage = gmailMessages.get(chatId);
		if (gmailMessage == null) {
			SearchTerm st = new HeaderTerm(GmailMessage.X_MESSAGE_ID, chatId);
			gmailMessage = retrieveFirstMessageMatchingSearchTerm(st);
			if (gmailMessage == null)
				return;
		}

		gmailMessage.delete();
	}

	@Override
	public GmailMessage retrieveFirstMessageMatchingSearchTerm(SearchTerm st) {
		GmailMessage gmailMessage;
		Folder folder = getSkypeChatFolder();
		Message[] foundMessages;
		try {
			foundMessages = folder.search(st);
		} catch (MessagingException e) {
			throw new MessageProcessingException(e);
		}
		if (foundMessages.length == 0)
			gmailMessage = new EmptyGmailMessage();
		else
			gmailMessage = new GmailMessageImpl((MimeMessage) foundMessages[0]);
		return gmailMessage;
	}

	@Override
	public void replaceMessageMatchingTerm(SearchTerm chatIndexSearchTerm,
			GmailMessage replacementMessage) {
		GmailMessage messageToReplace = retrieveFirstMessageMatchingSearchTerm(chatIndexSearchTerm);
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
