package skype;

import org.apache.log4j.Logger;

import utils.LoggerProvider;

import com.google.inject.Inject;

public class SkypeRecorder implements SkypeHistoryRecorder, SkypeApiChatVisitor {
	private final SkypeStorage skypeStorage;
	private final SkypeApi skypeApi;
	private final LoggerProvider loggerProvider;
	private Logger logger;

	@Inject
	public SkypeRecorder(SkypeApi skypeApi, SkypeStorage skypeStorage, LoggerProvider loggerProvider) {
		this.skypeApi = skypeApi;
		this.skypeStorage = skypeStorage;
		this.loggerProvider = loggerProvider;
	}

	@Override
	public void record() {
		if (!skypeApi.isRunning()) {
			getLogger().error("Skype must be running to run Skype2Gmail!");
			return;
		}

		try {
			skypeApi.accept(this);
		} finally {
			skypeStorage.close();
		}

		getLogger().info("Done.");
	}

	private Logger getLogger() {
		if (logger == null) {
			this.logger = loggerProvider.getLogger(getClass());
		}
		return logger;
	}

	@Override
	public void visit(SkypeChat skypeChat) {
		try {
			final StorageEntry previousEntry = skypeStorage
					.retrievePreviousEntryFor(skypeChat);
			boolean chatIsAlreadyRecorded = previousEntry.getChat()
					.getBodySignature().equals(skypeChat.getBodySignature());
			if (chatIsAlreadyRecorded) {
				getLogger().info("Entry " + skypeChat.getId() + " already up to date. Skipping.");
				return;
			}
	
			final SkypeChat newChat = previousEntry.getChat().merge(skypeChat);
	
			final StorageEntry storageEntry = skypeStorage.newEntry(newChat);
	
			storageEntry.store(new SkypeChatSetter(skypeChat));
			storageEntry.setLastModificationTime(skypeChat.getLastModificationTime());
			storageEntry.save();
	
			getLogger().info("Entry " + skypeChat.getId() + " written");
		} catch(RuntimeException e) {
			String message = String.format("An error was found processing message with the following id: %s",
					skypeChat.getId()
					);
			getLogger().error(message, e);
			getLogger().info("Message processing skipped");
		}
	}
}
