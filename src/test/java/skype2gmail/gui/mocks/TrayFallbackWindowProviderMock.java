package skype2gmail.gui.mocks;

import skype2gmail.gui.TrayFallbackWindowProvider;

public class TrayFallbackWindowProviderMock implements
		TrayFallbackWindowProvider {

	public boolean displayWasInvoked;

	@Override
	public void display() {
		displayWasInvoked = true;
	}

}
