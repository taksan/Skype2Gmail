package skype;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

public class SkypeRecorder implements SkypeHistoryRecorder {
	private static final Logger LOGGER = Logger.getLogger(SkypeRecorder.class);
	private final SkypeStorage skypeMedium;
	private final ChatContentBuilderFactory chatContentBuilderFactory;
	private final SkypeApi skypeApi;

	@Inject
	public SkypeRecorder(SkypeApi skypeApi, SkypeStorage skypeStorage,
			ChatContentBuilderFactory chatEntryBuilderFactory) {
		this.skypeApi = skypeApi;
		this.skypeMedium = skypeStorage;
		this.chatContentBuilderFactory = chatEntryBuilderFactory;
	}

	@Override
	public void record() {
		SkypeChat[] allChats;
		if (!skypeApi.isRunning()) {
			LOGGER.error("Skype must be running to run Skype2Gmail!");
			return;
		}
		allChats = skypeApi.getAllChats();
		LOGGER.info(String.format("Found %d chats.", allChats.length));

		for (SkypeChat chat : allChats) {
			ChatContentBuilder chatContentBuilder = chatContentBuilderFactory
					.produce(chat);

			StorageEntry storageEntry = skypeMedium.newEntry(chat);

			storageEntry.write(chatContentBuilder.getContent());
			storageEntry.setLastModificationTime(
					chatContentBuilder.getLastModificationTime());
			storageEntry.save();
		}
		LOGGER.info("Done.");
	}

	public SkypeStorage getSkypeStorage() {
		return this.skypeMedium;
	}
}
