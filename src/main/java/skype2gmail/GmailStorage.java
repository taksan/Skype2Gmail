package skype2gmail;

import gmail.GmailMessage;

import org.apache.log4j.Logger;

import skype.SkypeChat;
import skype.SkypeStorage;
import skype.StorageEntry;
import utils.LoggerProvider;

import com.google.inject.Inject;

public class GmailStorage implements SkypeStorage {

	private final GmailStorageEntryFactory entryFactory;
	
	private final GmailFolder skypeFolder;
	private final LoggerProvider loggerProvider;
	private final UserAuthProvider userAuthProvider;
	private Logger LOGGER;

	@Inject
	public GmailStorage(
			GmailStorageEntryFactory entryFactory, 
			GmailFolder skypeFolder,
			LoggerProvider loggerProvider,
			UserAuthProvider userAuthProvider) {
		this.entryFactory = entryFactory;
		this.skypeFolder = skypeFolder;
		this.loggerProvider = loggerProvider;
		this.userAuthProvider = userAuthProvider;
	}

	@Override
	public StorageEntry newEntry(SkypeChat chat) {
		return entryFactory.produce(chat);
	}

	@Override
	public StorageEntry retrievePreviousEntryFor(SkypeChat skypeChat) {
		GmailMessage entryMessage = skypeFolder.retrieveMessageEntryFor(skypeChat);
		return entryFactory.produce(skypeChat, entryMessage);
	}

	@Override
	public void open() {
		getLogger().info("Will send messages to " + userAuthProvider.getUser());
	}
	
	@Override
	public void close() {
		skypeFolder.close();
		getLogger().info("Messages sent to account " + userAuthProvider.getUser());
	}

	
	private Logger getLogger() {
		if (LOGGER != null)
			return LOGGER;
		LOGGER = loggerProvider.getLogger(getClass());
		return LOGGER;
		
	}
}
