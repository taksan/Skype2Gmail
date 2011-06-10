package skype2gmail;

import com.google.inject.ImplementedBy;

import mail.SkypeMailMessage;
import skype.commons.SkypeChat;
import skype.commons.StorageEntry;

@ImplementedBy(MailStorageEntryFactoryImpl.class)
public interface MailStorageEntryFactory {

	public StorageEntry produce(SkypeChat newChat, SkypeMailMessage previousChatMessage);

	public StorageEntry produce(SkypeChat newChat);

}
