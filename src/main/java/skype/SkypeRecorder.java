package skype;

import org.apache.log4j.Logger;

public class SkypeRecorder implements SkypeHistoryRecorder {
	private static final Logger LOGGER = Logger.getLogger(SkypeRecorder.class);
	private final SkypeStorage skypeMedium;
	private final ChatEntryBuilderFactory chatEntryBuilderFactory;
	private final SkypeApi skypeApi;

	public SkypeRecorder(SkypeApi skypeApi,
			SkypeStorage skypeStorage,
			ChatEntryBuilderFactory chatEntryBuilderFactory) {
		this.skypeApi = skypeApi;
		this.skypeMedium = skypeStorage;
		this.chatEntryBuilderFactory = chatEntryBuilderFactory;
	}

	@Override
	public void record() {
		SkypeChat[] allRecentChats;
		allRecentChats = skypeApi.getAllRecentChats();
		LOGGER.info(String.format("Found %d chats.", allRecentChats.length));

		for (SkypeChat chat : allRecentChats) {
			ChatEntryBuilder chatEntryBuilder = chatEntryBuilderFactory
					.produce(chat);

			StorageEntry storageEntry = skypeMedium.newEntry(chat);

			storageEntry.write(chatEntryBuilder.getContent());
			storageEntry.save();
		}
		LOGGER.info("Done.");
	}

}
