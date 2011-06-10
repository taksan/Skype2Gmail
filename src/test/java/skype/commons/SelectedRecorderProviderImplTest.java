package skype.commons;


import org.junit.Assert;
import org.junit.Test;

import skype.commons.mocks.Skype2GmailConfigContentsMock;
import skype2disk.FileSystemStorage;
import skype2disk.Skype2DiskModule;
import skype2gmail.MailStorage;
import skype2gmail.Skype2GmailConfigContents;
import skype2gmail.Skype2GmailModule;

public class SelectedRecorderProviderImplTest {
	@Test
	public void getSelected_ShouldReturnAccordingToConfiguration() {
		Skype2GmailConfigContents config = new Skype2GmailConfigContentsMock();
		SelectedRecorderProviderImpl subject = new SelectedRecorderProviderImpl(new String[0], config);
		
		config.setSelectedRecorderModule(Skype2GmailModule.class);
		SkypeRecorder firstSelected = (SkypeRecorder) subject.getSelected();
		Assert.assertEquals(MailStorage.class.getName(), firstSelected.getStorageType());
		
		config.setSelectedRecorderModule(Skype2DiskModule.class);
		SkypeRecorder secondSelected = (SkypeRecorder) subject.getSelected();
		Assert.assertEquals(FileSystemStorage.class.getName(), secondSelected.getStorageType());
	}
}
