package skype.commons;

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
	private final SkypeStorage skypeStorage;
	@Inject
	public LastSynchronizationProviderImpl(Skype2GmailConfigDir configDir, 
			LoggerProvider loggerProvider,
			SkypeStorage storage)
	{
		this.configDir = configDir;
		this.loggerProvider = loggerProvider;
		this.skypeStorage = storage;
	}
	
	@Override
	public Date getLastSynch() {
		File syncPlaceHolder = getPlaceHolderFile();
		Calendar c = Calendar.getInstance();
		if (!syncPlaceHolder.exists()) {
			getLogger().info(String.format("No sync place holder found for this storage (%s)", syncPlaceHolder));
			c.set(Calendar.YEAR, 1900);
			return c.getTime();
		}
		long lastModifiedTimeInMillis = syncPlaceHolder.lastModified();
		return new Date(lastModifiedTimeInMillis);
	}
	@Override
	public void updateSynch() {
		File syncPlaceHolder = getPlaceHolderFile();
		try {
			FileUtils.touch(syncPlaceHolder);
			long lastModified = syncPlaceHolder.lastModified();
			String lastSyncDate = new Date(lastModified).toString();
			String placeHolderContent = String.format("Last synchronization at %s.\n", lastSyncDate);
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

	File getPlaceHolderFile() {
		return configDir.getFileUnder("/syncs/"+skypeStorage.getSyncId());
	}
}
