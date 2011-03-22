package skype2gmail;

import gmail.GmailFolder;
import gmail.GmailMessage;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class GmailFolderImpl implements GmailFolder {

	private final Folder root;
	private final Map<String, GmailMessage> gmailMessages = new LinkedHashMap<String, GmailMessage>();
	private GmailMessage[] retrievedMessages;

	public GmailFolderImpl(Folder root) {
		this.root = root;
	}

	@Override
	public void appendMessage(GmailMessage gmailMessage) {
		Message[] msgs = new javax.mail.Message[] { gmailMessage.getMimeMessage() };
		try {
			root.appendMessages(msgs);
			replaceOldMessage(gmailMessage);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	@Override
	public GmailMessage[] getMessages() {
		try {
			if (retrievedMessages != null) {
				return retrievedMessages;
			}
			for (Message message : root.getMessages()) {
				GmailMessage  gmailMessage = new GmailMessage((MimeMessage)message);
				gmailMessages.put(gmailMessage.getChatId(), gmailMessage);
			}
			retrievedMessages = gmailMessages.values().toArray(new GmailMessage[0]);
			return retrievedMessages;
		} catch (MessagingException e) {
			throw new RuntimeException(e);
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
			throw new RuntimeException(e);
		}
	}
}
