package skype;

import org.apache.commons.lang.time.DurationFormatUtils;
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

		long startTime = System.currentTimeMillis();
		skypeStorage.open();
		try {
			try {
				skypeApi.accept(this);
			}
			catch(ApplicationException e) {
				getLogger().error(e);
			}
		} finally {
			skypeStorage.close();
		}
		long endTime = System.currentTimeMillis();
		long elapsedMills = endTime - startTime;
		getLogger().info("Took " + DurationFormatUtils.formatDuration(elapsedMills, "H:m:s"));
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
			if (skypeChat.getChatMessages().size() == 0) {
				getLogger().info("Entry " + skypeChat.getId() + " skipped because it has no messages.");
				return;
			}
			final StorageEntry previousEntry = skypeStorage.retrievePreviousEntryFor(skypeChat);
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
		} catch(MessageProcessingException e) {
			String message = String.format(
					"An error was found processing message with the following id: %s",
					skypeChat.getId()
					);
			getLogger().error(message, e);
			getLogger().info("Message processing skipped");
		}
	}
}
