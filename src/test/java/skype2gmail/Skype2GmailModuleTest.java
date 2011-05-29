package skype2gmail;

import java.io.IOException;

import junit.framework.Assert;
import mail.SkypeMailFolder;
import mail.mocks.FolderMock;

import org.junit.Test;

import skype.commons.AbstractRecordingTest;
import skype.commons.SkypeHistoryRecorder;
import skype2gmail.mocks.Skype2GmailModuleMockingSkypeApi;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Skype2GmailModuleTest extends AbstractRecordingTest {

	@Test
	public void testInjections() {
		final Skype2GmailModuleCommons skype2GmailModule = new Skype2GmailModule();
		final Injector injector = Guice.createInjector(skype2GmailModule);

		injector.getInstance(SkypeHistoryRecorder.class);
	}
	
	@Test
	public void testModuleMockingSkypeApi() throws IOException {
		final Injector injector = Guice.createInjector(new Skype2GmailModuleMockingSkypeApi());
		
		testRecording(injector);
		FolderMock folderProvider = (FolderMock) injector.getInstance(SkypeMailFolder.class);
		
		Assert.assertEquals(3, folderProvider.getMessages().length);
	}
}
