package skype2gmail;

import gmail.GmailFolder;
import gmail.GmailMessage;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

public class GmailFolderImpl implements GmailFolder {

	private final Folder root;

	public GmailFolderImpl(Folder root) {
		this.root = root;
	}

	@Override
	public void appendMessage(GmailMessage gmailMessage) {
		Message[] msgs = new javax.mail.Message[] { gmailMessage.toMimeMessage() };
		try {
			root.appendMessages(msgs);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public javax.mail.Message[] getMessages() {
		try {
			return root.getMessages();
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}
