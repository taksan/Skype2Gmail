package skype;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

public class SkypeRecorder implements SkypeHistoryRecorder, SkypeApiChatVisitor {
	private static final Logger LOGGER = Logger.getLogger(SkypeRecorder.class);
	private final SkypeStorage skypeStorage;
	private final SkypeApi skypeApi;

	@Inject
	public SkypeRecorder(SkypeApi skypeApi, SkypeStorage skypeStorage) {
		this.skypeApi = skypeApi;
		this.skypeStorage = skypeStorage;
	}

	@Override
	public void record() {
		if (!skypeApi.isRunning()) {
			LOGGER.error("Skype must be running to run Skype2Gmail!");
			return;
		}

		try {
			skypeApi.accept(this);
		} finally {
			skypeStorage.close();
		}

		LOGGER.info("Done.");
	}

	@Override
	public void visit(SkypeChat skypeChat) {
		final StorageEntry previousEntry = skypeStorage
				.retrievePreviousEntryFor(skypeChat);
		boolean chatIsAlreadyRecorded = previousEntry.getChat()
				.getBodySignature().equals(skypeChat.getBodySignature());
		if (chatIsAlreadyRecorded) {
			LOGGER.info("Entry " + skypeChat.getId()
					+ " already up to date. Skipping.");
			return;
		}

		final SkypeChat newChat = previousEntry.getChat().merge(skypeChat);

		final StorageEntry storageEntry = skypeStorage.newEntry(newChat);

		storageEntry.store(new SkypeChatSetter(skypeChat));
		storageEntry.setLastModificationTime(skypeChat
				.getLastModificationTime());
		storageEntry.save();

		LOGGER.info("Entry " + skypeChat.getId() + " written");
	}
}
