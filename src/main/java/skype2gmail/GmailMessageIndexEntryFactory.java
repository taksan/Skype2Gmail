package skype2gmail;

import skype.SkypeChat;
import gmail.GmailMessage;

public interface GmailMessageIndexEntryFactory {

	GmailMessage produce(GmailFolder nonIndexedGmailFolder,
			SkypeChat skypeChat, String previousChatSignature);
}
