package skype;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

public class SkypeRecorder implements SkypeHistoryRecorder, SkypeApiChatVisitor {
	private static final Logger LOGGER = Logger.getLogger(SkypeRecorder.class);
	private final SkypeStorage skypeMedium;
	private final SkypeApi skypeApi;

	@Inject
	public SkypeRecorder(SkypeApi skypeApi, SkypeStorage skypeStorage) {
		this.skypeApi = skypeApi;
		this.skypeMedium = skypeStorage;
	}

	@Override
	public void record() {
		if (!skypeApi.isRunning()) {
			LOGGER.error("Skype must be running to run Skype2Gmail!");
			return;
		}
		
		skypeApi.accept(this);
		
		LOGGER.info("Done.");
	}
	
	@Override
	public void visit(SkypeChat skypeChat) {
		StorageEntry storageEntry = skypeMedium.newEntry(skypeChat);

		storageEntry.write(skypeChat);
		
		storageEntry.setLastModificationTime(skypeChat.getLastModificationTime());
		storageEntry.save();
		LOGGER.info("Entry " + skypeChat.getId() + " written");
	}

	public SkypeStorage getSkypeStorage() {
		return this.skypeMedium;
	}
}
