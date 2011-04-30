package skype2gmail;

import mail.SkypeMailMessage;
import skype.SkypeChat;
import skype.StorageEntry;

public interface MailStorageEntryFactory {

	public StorageEntry produce(SkypeChat newChat, SkypeMailMessage previousChatMessage);

	public StorageEntry produce(SkypeChat newChat);

}
