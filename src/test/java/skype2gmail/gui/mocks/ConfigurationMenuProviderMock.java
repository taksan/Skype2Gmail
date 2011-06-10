package skype2gmail.gui.mocks;

import skype2gmail.gui.ConfigurationMenuProvider;


public class ConfigurationMenuProviderMock implements ConfigurationMenuProvider {

	public boolean displayWasInvoked;

	@Override
	public void display() {
		displayWasInvoked = true;
	}

}
