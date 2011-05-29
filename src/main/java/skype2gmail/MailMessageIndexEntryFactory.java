package skype2gmail;

import mail.SkypeMailFolder;
import mail.SkypeMailMessage;
import skype.commons.SkypeChat;

public interface MailMessageIndexEntryFactory {

	SkypeMailMessage produce(SkypeMailFolder nonIndexedGmailFolder,
			SkypeChat skypeChat, String previousChatSignature);
}
