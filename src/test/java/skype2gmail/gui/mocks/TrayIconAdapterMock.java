package skype2gmail.gui.mocks;

import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionListener;
import java.net.URL;

import org.apache.commons.lang.NotImplementedException;

import tray.TrayIconAdapter;

public class TrayIconAdapterMock implements TrayIconAdapter {

	@Override
	public void displayMessage(String caption, String text,
			MessageType messageType) {
		throw new NotImplementedException();
	}

	@Override
	public void setImageAutoSize(boolean autoSize) {
		throw new NotImplementedException();
	}

	@Override
	public void addActionListener(ActionListener actionListener) {
		
	}

	@Override
	public void setImage(URL imageUrl) {
		throw new NotImplementedException();
	}

}
