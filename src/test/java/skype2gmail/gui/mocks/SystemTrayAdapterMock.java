package skype2gmail.gui.mocks;

import java.awt.PopupMenu;
import java.net.URL;

import org.apache.commons.lang.NotImplementedException;

import tray.SystemTrayAdapter;
import tray.TrayIconAdapter;

public class SystemTrayAdapterMock implements SystemTrayAdapter {
		@Override
		public void remove(TrayIconAdapter trayIcon) {
			throw new NotImplementedException();
		}

		@Override
		public TrayIconAdapter createAndAddTrayIcon(URL imageUrl, String tooltip,
				PopupMenu popup) {
			return new TrayIconAdapterMock();
		}
	}