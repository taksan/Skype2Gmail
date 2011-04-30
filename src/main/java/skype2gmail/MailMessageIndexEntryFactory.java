package skype2gmail;

import mail.SkypeMailFolder;
import mail.SkypeMailMessage;
import skype.SkypeChat;

public interface MailMessageIndexEntryFactory {

	SkypeMailMessage produce(SkypeMailFolder nonIndexedGmailFolder,
			SkypeChat skypeChat, String previousChatSignature);
}
