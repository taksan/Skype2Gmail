package skype;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import skype.mocks.SkypeStorageMock;
import skype2disk.Skype2GmailConfigDir;
import skype2disk.mocks.BasePathMock;
import testutils.DateHelper;
import utils.SimpleLoggerProvider;

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
	public void after() throws IOException {
		FileUtils.deleteDirectory(new File(basePath.getPath()));
	}
	
	@Test
	public void testGetLastSynch() throws IOException {
		SkypeStorage firstStorage = new SkypeStorageMock("mock-1");
		int lastSyncYear = 1972;
		checkStorageTime(firstStorage, lastSyncYear);
	}

	@Test
	public void testUpdateSynch() {
		SkypeStorage mock = new SkypeStorageMock("mock");
		LastSynchronizationProviderImpl subject = new LastSynchronizationProviderImpl(mockConfigDir, null, mock);
		subject.updateSynch();
		File placeHolderFile = subject.getPlaceHolderFile();
		Assert.assertTrue(placeHolderFile.exists());
	}
	
	@Test
	public void testGetLastSynchForDistinctStorages() throws IOException {
		SkypeStorage firstStorage = new SkypeStorageMock("mock-1");
		int lastSyncYear = 1972;
		checkStorageTime(firstStorage, lastSyncYear);
		
		SkypeStorage secondStorage = new SkypeStorageMock("mock-2");
		int lastSyncYear2 = 1973;
		checkStorageTime(secondStorage, lastSyncYear2);
		
		File directory = mockConfigDir.getFileUnder("syncs");
		Assert.assertTrue("syncs directory should hava been created", directory.exists());
		String[] listFiles = directory.list();
		Arrays.sort(listFiles);
		String syncFiles = ArrayUtils.toString(listFiles);
		
		Assert.assertEquals("{mock-1,mock-2}", syncFiles);
	}

	private void checkStorageTime(SkypeStorage firstStorage,
			int lastSyncYear) {
		SimpleLoggerProvider loggerProvider = new SimpleLoggerProvider();
		LastSynchronizationProviderImpl subject = 
			new LastSynchronizationProviderImpl(mockConfigDir, loggerProvider, firstStorage);

		long mock1StorageSyncTime = getPlaceHolderForFileName(subject.getPlaceHolderFile(), lastSyncYear);
		final Date lastTime = new Date(mock1StorageSyncTime);
		Date lastSynch = subject.getLastSynch();
		Assert.assertEquals(lastTime.toString(), lastSynch.toString());
	}

	private long getPlaceHolderForFileName(File placeHolder, int year) {
		try {
			FileUtils.touch(placeHolder);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		long lastSyncTimeInMillis = DateHelper.makeDateInMillis(year, 12, 1, 3, 6, 9);
		placeHolder.setLastModified(lastSyncTimeInMillis);
		return lastSyncTimeInMillis;
	}
}
