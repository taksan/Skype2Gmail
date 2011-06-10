package skype.commons;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.log4j.Logger;

import skype.exceptions.ApplicationException;
import skype.exceptions.MessageProcessingException;
import utils.LoggerProvider;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SkypeRecorder implements SkypeHistoryRecorder, SkypeApiChatVisitor {
	private final SkypeStorage skypeStorage;
	private final SkypeApi skypeApi;
	private final LoggerProvider loggerProvider;
	private Logger logger;
	private final LastSynchronizationProvider lastSynchronizationProvider;
	private int updatedChats = 0;
	private int chatsWithProblems = 0;

	@Inject
	public SkypeRecorder(SkypeApi skypeApi, 
			SkypeStorage skypeStorage, 
			LoggerProvider loggerProvider, 
			LastSynchronizationProvider lastSynchronizationProvider) {
		this.skypeApi = skypeApi;
		this.skypeStorage = skypeStorage;
		this.loggerProvider = loggerProvider;
		this.lastSynchronizationProvider = lastSynchronizationProvider;
	}

	@Override
	public void record() {
		if (!skypeApi.isRunning()) {
			getLogger().error("Skype must be running to run Skype2Gmail!");
			return;
		}

		getLogger().info("Starting synchronization...");
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
		final String message;
		if(updatedChats == 0) {
			if (chatsWithProblems > 0) {
				message = "No messages were synchronized.";
			}
			else
				message = "No messages were synchronized. All of them were up to date.";
		}
		else {
			message = updatedChats + " messages have been synchronized.";
		}
		getLogger().info(message);
		if (chatsWithProblems > 0) {
			getLogger().warn(chatsWithProblems+ " chat messages were not synchronized because of errors");
		}
		getLogger().info("Done.");
		this.lastSynchronizationProvider.updateSynch();
	}

	@Override
	public void visit(SkypeChat skypeChat) {
		try {
			if (skypeChat.getChatMessages().size() == 0) {
				getVerboseLogger().warn("Entry " + skypeChat.getId() + " skipped because it has no messages.");
				return;
			}
			final StorageEntry previousEntry = skypeStorage.retrievePreviousEntryFor(skypeChat);
			boolean chatIsAlreadyRecorded = previousEntry.getChat()
					.getBodySignature().equals(skypeChat.getBodySignature());
			if (chatIsAlreadyRecorded) {
				getVerboseLogger().info("Entry " + skypeChat.getId() + " already up to date. Skipping.");
				return;
			}
	
			final SkypeChat newChat = previousEntry.getChat().merge(skypeChat);
			if (newChat.getBodySignature().equals(previousEntry.getChat().getBodySignature())) {
				return;
			}
	
			final StorageEntry storageEntry = skypeStorage.newEntry(newChat);
	
			storageEntry.store(new SkypeChatSetter(skypeChat));
			storageEntry.setLastModificationTime(skypeChat.getLastModificationTime());
			storageEntry.save();
	
			getVerboseLogger().info("Entry " + skypeChat.getId() + " written");
			updatedChats ++;
		} catch(MessageProcessingException e) {
			String message = String.format(
					"An error was found processing message with the following id: %s",
					skypeChat.getId()
					);
			getLogger().error(message, e);
			getLogger().info("Message processing skipped");
			chatsWithProblems ++;
		}
	}
	
	public String getStorageType() {
		return skypeStorage.getClass().getName();
	}

	private Logger getLogger() {
		if (logger == null)
			logger = loggerProvider.getPriorityLogger(getClass());
		return logger;
	}
	
	private Logger getVerboseLogger() {
		return loggerProvider.getLogger(getClass().toString()+"Verbose");
	}
}