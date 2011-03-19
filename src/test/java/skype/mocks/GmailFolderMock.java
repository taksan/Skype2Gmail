package skype.mocks;

import gmail.GmailFolder;
import gmail.GmailMessage;

import java.util.LinkedList;
import java.util.List;

public class GmailFolderMock implements GmailFolder {
	
	private final List<GmailMessage> messageList;

	public GmailFolderMock() {
		messageList = new LinkedList<GmailMessage>();
	}

	@Override
	public void appendMessage(GmailMessage gmailMessage) {
		messageList.add(gmailMessage);
	}

	@Override
	public GmailMessage[] getMessages() {
		return messageList.toArray(new GmailMessage[0]);
	}

}
