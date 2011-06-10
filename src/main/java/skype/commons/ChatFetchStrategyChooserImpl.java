package skype.commons;

import java.util.Calendar;
import java.util.Date;

import skype2gmail.Skype2GmailConfigContents;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ChatFetchStrategyChooserImpl implements ChatFetchStrategyChooser {
	
	private LastSynchronizationProvider lastSynchronizationProvider;
	private final Skype2GmailConfigContents config;

	@Inject
	public ChatFetchStrategyChooserImpl(Skype2GmailConfigContents config, LastSynchronizationProvider lastSynchronizationProvider)
	{
		this.config = config;
		this.lastSynchronizationProvider = lastSynchronizationProvider;
	}

	@Override
	public boolean catFetchJustTheRecentChats() {
		if (config.isSyncWithRecentsDisabled()) {
			return false;
		}
		Calendar cal24hAgo = Calendar.getInstance();
		cal24hAgo.add(Calendar.DATE, -1);
		Date time24hoursAgo = cal24hAgo.getTime();
		
		Date lastSynch = lastSynchronizationProvider.getLastSynch();
		return lastSynch.compareTo(time24hoursAgo) > 0;
	}

}
