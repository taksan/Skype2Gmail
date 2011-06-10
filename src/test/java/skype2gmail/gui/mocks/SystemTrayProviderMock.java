package skype2gmail.gui.mocks;

import tray.SystemTrayAdapter;
import tray.SystemTrayProviderInterface;

public class SystemTrayProviderMock implements  SystemTrayProviderInterface {

	public boolean isTraySuppported = true;

	@Override
	public SystemTrayAdapter getSystemTray() {
		return new SystemTrayAdapterMock();
	}

	@Override
	public boolean isSupported() {
		return isTraySuppported ;
	}
}
