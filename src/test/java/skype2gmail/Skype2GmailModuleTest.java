package skype2gmail;

import gmail.GmailFolder;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

import skype.AbstractRecordingTest;
import skype.SkypeHistoryRecorder;
import skype.mocks.RootFolderProviderMock;
import skype.mocks.Skype2GmailModuleMockingSkypeApi;

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
		
//		GmailMessageProviderMock gmailMessageProvider = (GmailMessageProviderMock) injector.getInstance(GmailMessageProvider.class);
		
		testRecording(injector);
		RootFolderProviderMock folderProvider = (RootFolderProviderMock) injector.getInstance(RootFolderProvider.class);
		GmailFolder gmailFolder = folderProvider.getInstance();
		
		Assert.assertEquals(4, gmailFolder.getMessages().length);
	}
}