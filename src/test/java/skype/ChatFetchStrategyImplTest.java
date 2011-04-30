package skype;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.NotImplementedException;
import org.junit.Assert;
import org.junit.Test;

import skype2gmail.Skype2GmailConfigContents;

public class ChatFetchStrategyImplTest {

	@Test
	public void testWillUseRecent() {
		Skype2GmailConfigContents configMock = new Skype2GmailConfigContentsMock();
		final int timeOffset = -1;
		LastSynchronizationProvider lastSynchronizationProviderMock = 
			getSynchProvider(timeOffset, Calendar.HOUR);
		
		ChatFetchStrategyChooserImpl fetchStrategy = new ChatFetchStrategyChooserImpl(configMock, lastSynchronizationProviderMock);
		//setLastModified
		Assert.assertTrue(fetchStrategy.catFetchJustTheRecentChats());
	}
	
	@Test
	public void testWontUseRecentBecauseOfConfiguration() {
		Skype2GmailConfigContentsMock configMock = new Skype2GmailConfigContentsMock();
		configMock.setSyncWithRecentsDisabled(true);
		final int timeOffset = -1;
		LastSynchronizationProvider lastSynchronizationProviderMock = 
			getSynchProvider(timeOffset, Calendar.HOUR);
		
		ChatFetchStrategyChooserImpl fetchStrategy = new ChatFetchStrategyChooserImpl(configMock, lastSynchronizationProviderMock);
		//setLastModified
		Assert.assertFalse(fetchStrategy.catFetchJustTheRecentChats());
	}

	private LastSynchronizationProvider getSynchProvider(final int timeOffset, final int timeUnit) {
		LastSynchronizationProvider lastSynchronizationProviderMock = new LastSynchronizationProvider() {

			@Override
			public Date getLastSynch() {
				Calendar cal  = Calendar.getInstance();
				cal.add(timeUnit, timeOffset);
				return cal.getTime();
			}

			@Override
			public void updateSynch() {
				throw new NotImplementedException();
			}
		};
		return lastSynchronizationProviderMock;
	}
}
