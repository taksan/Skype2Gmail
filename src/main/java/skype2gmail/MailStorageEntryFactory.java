package skype2gmail;

import mail.SkypeMailMessage;
import skype.commons.SkypeChat;
import skype.commons.StorageEntry;

public interface MailStorageEntryFactory {

	public StorageEntry produce(SkypeChat newChat, SkypeMailMessage previousChatMessage);

	public StorageEntry produce(SkypeChat newChat);

}
