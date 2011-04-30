package skype;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import skype2disk.Skype2GmailConfigDir;
import utils.LoggerProvider;

import com.google.inject.Inject;

public class LastSynchronizationProviderImpl implements LastSynchronizationProvider {

	Skype2GmailConfigDir configDir;
	private final LoggerProvider loggerProvider;
	@Inject
	public LastSynchronizationProviderImpl(Skype2GmailConfigDir configDir, LoggerProvider loggerProvider)
	{
		this.configDir = configDir;
		this.loggerProvider = loggerProvider;
	}
	
	@Override
	public Date getLastSynch() {
		File syncPlaceHolder = configDir.getFileUnder(LastSynchronizationProvider.LAST_SYNCH_PLACE_HOLDER);
		Calendar c = Calendar.getInstance();
		if (!syncPlaceHolder.exists()) {
			getLogger().info("No sync place holder found.");
			c.set(Calendar.YEAR, 1900);
			return c.getTime();
		}
		long lastModifiedTimeInMillis = syncPlaceHolder.lastModified();
		return new Date(lastModifiedTimeInMillis);
	}

	@Override
	public void updateSynch() {
		File syncPlaceHolder = configDir.getFileUnder(LastSynchronizationProvider.LAST_SYNCH_PLACE_HOLDER);
		try {
			FileUtils.touch(syncPlaceHolder);
			long lastModified = syncPlaceHolder.lastModified();
			String lastSyncDate = new Date(lastModified).toString();
			String placeHolderContent = String.format("Last synchronization at %s.", lastSyncDate);
			FileUtils.writeStringToFile(syncPlaceHolder, placeHolderContent);
		} catch (IOException e) {
			String absolutePath = syncPlaceHolder.getAbsolutePath();
			String errorMessage = "Failed to update sync place holder file: "+absolutePath;
			getLogger().warn(errorMessage, e);
		}
	}

	private Logger getLogger() {
		return loggerProvider.getPriorityLogger(getClass());
	}

}
