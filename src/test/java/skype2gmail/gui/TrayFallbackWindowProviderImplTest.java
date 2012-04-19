package skype2gmail.gui;

import java.awt.Component;

import javax.swing.JButton;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import skype2gmail.gui.mocks.ConfigurationMenuProviderMock;

public class TrayFallbackWindowProviderImplTest {
	ConfigurationMenuProviderMock configurationMenuMock = new ConfigurationMenuProviderMock();
	TrayFallbackWindowProviderImpl subject = new TrayFallbackWindowProviderImpl(configurationMenuMock);
	
	@Test
	public void testDisplay_WindowShouldBeVisible() {
		Assert.assertTrue(subject.isShowing());
	}
	
	@Test
	public void windowName_ShouldBeSkype2Gmail() {
		Assert.assertEquals("Skype2Gmail", subject.getTitle());
	}
	
	@Test
	public void configureButton_ShouldExists() {
		JButton configureButton = getConfigureButton();
		Assert.assertNotNull("Configure button not found", configureButton);
	}
	
	@Test
	public void configureButtonClick_ShouldDisplayConfigurationWindow() {
		JButton configureButton = getConfigureButton();
		configureButton.doClick();
		Assert.assertTrue("configuration.display should have been invoked", configurationMenuMock.displayWasInvoked);
	}

	private JButton getConfigureButton() {
		JButton configureButton = null;

		String configureButtonName = "configure-button";
		
		Component[] components = subject.getContentPane().getComponents();
		for (Component component : components) {
			if (component instanceof JButton && configureButtonName.equals(component.getName())) {
				configureButton = (JButton) component;
			}
		}
		return configureButton;
	}

	@Before
	public void setup() {
		subject.display();
	}
	
	@After
	public void teardown() {
		subject.dispose();
	}
}
