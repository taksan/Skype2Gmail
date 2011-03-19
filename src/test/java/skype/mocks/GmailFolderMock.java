package skype.mocks;

import gmail.GmailFolder;
import gmail.GmailMessage;

import java.util.LinkedList;
import java.util.List;

import javax.mail.Message;
import javax.mail.internet.MimeMessage;

public class GmailFolderMock implements GmailFolder {
	
	private final List<MimeMessage> messageList;

	public GmailFolderMock() {
		messageList = new LinkedList<MimeMessage>();
	}

	@Override
	public void appendMessage(GmailMessage gmailMessage) {
		messageList.add(gmailMessage.toMimeMessage());
	}

	@Override
	public Message[] getMessages() {
		return messageList.toArray(new MimeMessage[0]);
	}

}
