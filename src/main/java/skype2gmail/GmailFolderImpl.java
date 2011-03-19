package skype2gmail;

import java.util.LinkedList;
import java.util.List;

import gmail.GmailFolder;
import gmail.GmailMessage;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class GmailFolderImpl implements GmailFolder {

	private final Folder root;

	public GmailFolderImpl(Folder root) {
		this.root = root;
	}

	@Override
	public void appendMessage(GmailMessage gmailMessage) {
		Message[] msgs = new javax.mail.Message[] { gmailMessage.getMimeMessage() };
		try {
			root.appendMessages(msgs);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public GmailMessage[] getMessages() {
		try {
			List<GmailMessage> gmailMessages = new LinkedList<GmailMessage>();
			for (Message message : root.getMessages()) {
				gmailMessages.add(new GmailMessage((MimeMessage)message));
			}
			return gmailMessages.toArray(new GmailMessage[0]);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}
