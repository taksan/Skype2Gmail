package skype;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import skype2disk.Skype2GmailConfigDir;
import skype2disk.mocks.BasePathMock;
import testutils.DateHelper;

public class LastSynchronizationProviderImplTest {
	private BasePathMock basePath;
	private Skype2GmailConfigDir mockConfigDir;

	@Before
	public void before()
	{
		basePath = new BasePathMock();
		mockConfigDir = new Skype2GmailConfigDir(basePath);
	}
	
	@After
	public void after() {
		basePath.delete();
	}
	
	@Test
	public void testGetLastSynch() throws IOException {
		File placeHolder = mockConfigDir.getFileUnder(LastSynchronizationProvider.LAST_SYNCH_PLACE_HOLDER);

		FileUtils.touch(placeHolder);
		long lastSyncTimeInMillis = DateHelper.makeDateInMillis(1972, 12, 1, 3, 6, 9);
		placeHolder.setLastModified(lastSyncTimeInMillis);

		LastSynchronizationProviderImpl subject = 
			new LastSynchronizationProviderImpl(mockConfigDir, null);

		Date lastSynch = subject.getLastSynch();

		final Date lastTime = new Date(lastSyncTimeInMillis);
		Assert.assertEquals(lastTime.toString(), lastSynch.toString());
	}

	@Test
	public void testUpdateSynch() {
		File placeHolder = mockConfigDir.getFileUnder(LastSynchronizationProvider.LAST_SYNCH_PLACE_HOLDER);
		Assert.assertFalse(placeHolder.exists());
		
		LastSynchronizationProvider subject = new LastSynchronizationProviderImpl(mockConfigDir, null);
		subject.updateSynch();
		Assert.assertTrue(placeHolder.exists());
	}
}
