package skype2gmail.gui;

import org.junit.Assert;
import org.junit.Test;

import skype2gmail.gui.mocks.Skype2GmailClickActionProviderMock;
import skype2gmail.gui.mocks.Skype2GmailMenuProviderMock;
import skype2gmail.gui.mocks.SystemTrayProviderMock;
import skype2gmail.gui.mocks.TrayFallbackWindowProviderMock;


public class Skype2GmailTrayProviderTest {
	@Test
	public void getTray_ShouldGoWithoutAnyExceptions() {
		SystemTrayProviderMock systemTrayProvider = new SystemTrayProviderMock();
		Skype2GmailMenuProviderMock trayMenuProvider = new Skype2GmailMenuProviderMock();
		Skype2GmailClickActionProviderMock clickActionProvider = new Skype2GmailClickActionProviderMock();
		
		Skype2GmailTrayProviderImpl provider = new Skype2GmailTrayProviderImpl(
				systemTrayProvider,
				trayMenuProvider,
				clickActionProvider,
				null);
		
		provider.createTray();
		
		Assert.assertTrue(trayMenuProvider.getMenuWasInvoked);
		Assert.assertTrue(clickActionProvider.getWasInvoked);
	}
	
	@Test
	public void getTrayWhenNotSupported_ShouldDisplayAnApplicationWindow()
	{
		Skype2GmailMenuProviderMock trayMenuProvider = new Skype2GmailMenuProviderMock();
		Skype2GmailClickActionProviderMock clickActionProvider = new Skype2GmailClickActionProviderMock();
		
		SystemTrayProviderMock systemTrayProvider = new SystemTrayProviderMock();
		systemTrayProvider.isTraySuppported = false;
		
		TrayFallbackWindowProviderMock trayFallbackWindow = new TrayFallbackWindowProviderMock();
		Skype2GmailTrayProviderImpl provider = new Skype2GmailTrayProviderImpl(
				systemTrayProvider,
				trayMenuProvider,
				clickActionProvider,
				trayFallbackWindow);
		
		provider.createTray();
		Assert.assertTrue(trayFallbackWindow.displayWasInvoked);
	}
}
