package skype2gmail;


import mail.SkypeMailFolder;
import mail.SkypeMailMessage;

import org.apache.log4j.Logger;

import skype.SkypeChat;
import skype.SkypeStorage;
import skype.StorageEntry;
import utils.LoggerProvider;

import com.google.inject.Inject;

public class MailStorage implements SkypeStorage {

	private final MailStorageEntryFactory entryFactory;
	
	private final SkypeMailFolder skypeFolder;
	private final LoggerProvider loggerProvider;
	private final UserAuthProvider userAuthProvider;
	private Logger LOGGER;

	@Inject
	public MailStorage(
			MailStorageEntryFactory entryFactory, 
			SkypeMailFolder skypeFolder,
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
		SkypeMailMessage entryMessage = skypeFolder.retrieveMessageEntryFor(skypeChat);
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
