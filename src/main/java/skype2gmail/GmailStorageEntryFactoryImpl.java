package skype2gmail;

import com.google.inject.Inject;

import skype.SkypeChat;
import skype.SkypeChatDateFormat;
import skype.StorageEntry;

public class GmailStorageEntryFactoryImpl implements GmailStorageEntryFactory {

	private final SkypeChatDateFormat chatDateFormat;
	private final SessionProvider sessionProvider;
	private final GmailStore rootFolderProvider;
	
	@Inject
	public GmailStorageEntryFactoryImpl(SkypeChatDateFormat chatDateFormat,
			SessionProvider sessionProvider,
			GmailStore rootFolderProvider) {
		this.chatDateFormat = chatDateFormat;
		this.sessionProvider = sessionProvider;
		this.rootFolderProvider = rootFolderProvider;
	}

	@Override
	public StorageEntry produce(SkypeChat chat) {
		return new GmailStorageEntry(sessionProvider, rootFolderProvider, chat, this.chatDateFormat);
	}

}
