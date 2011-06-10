package skype2gmail.gui;

import java.awt.event.ActionListener;

import junit.framework.Assert;

import org.junit.Test;

import skype2gmail.gui.mocks.ConfigurationMenuProviderMock;

public class Skype2GmailClickActionProviderImplTest {
	@Test
	public void testGet_ShouldReturnActionThatOpensConfigurationMenu() {
		ConfigurationMenuProviderMock configMenuProvider = new ConfigurationMenuProviderMock(); 
		Skype2GmailClickActionProviderImpl subject = new Skype2GmailClickActionProviderImpl(configMenuProvider);
		ActionListener actionListener = subject.get();
		actionListener.actionPerformed(null);
		
		Assert.assertTrue(configMenuProvider.displayWasInvoked);
	}
}
