package skype2gmail.gui;

import java.lang.reflect.Proxy;

import junit.framework.Assert;

import org.junit.Test;

import skype.commons.SelectedRecorderProvider;
import skype2gmail.gui.mocks.SelectedRecorderProviderMock;
import skype2gmail.gui.mocks.TimedTaskExecutorMock;
import skype2gmail.mocks.SKypeHistoryRecorderMock;
import testutils.InvocationLoggerMocker;

public class Skype2GmailGuiTest {
	@Test
	public void testRun_ShouldDisplayTrayAndStartSynchronizing() {
		Skype2GmailTrayProvider trayProvider =
			InvocationLoggerMocker.create(Skype2GmailTrayProvider.class);
		InvocationLoggerMocker invocationLogger = (InvocationLoggerMocker) Proxy.getInvocationHandler(trayProvider);
			
		TimedTaskExecutorMock timerProvider = new TimedTaskExecutorMock();
		
		SelectedRecorderProvider skypeRecorderRegistry = new SelectedRecorderProviderMock();
				
		Skype2GmailGui subject = new Skype2GmailGui(
				trayProvider, 
				timerProvider, 
				skypeRecorderRegistry);
		
		subject.run();
		Assert.assertEquals("createTray", invocationLogger.getInvocations());
		
		timerProvider.tick();
		SKypeHistoryRecorderMock selectedRecorder = (SKypeHistoryRecorderMock) skypeRecorderRegistry.getSelected(); 
		Assert.assertTrue("record method should have been invoked", selectedRecorder.recordWasInvoked);
	}
}
