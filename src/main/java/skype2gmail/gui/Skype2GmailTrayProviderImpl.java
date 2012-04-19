package skype2gmail.gui;

import java.awt.PopupMenu;
import java.net.URL;

import tray.SystemTrayAdapter;
import tray.SystemTrayProviderInterface;
import tray.TrayIconAdapter;

import com.google.inject.Inject;

public class Skype2GmailTrayProviderImpl implements Skype2GmailTrayProvider {

	private Skype2GmailMenuProvider trayMenuProvider;
	private final SystemTrayProviderInterface systemTrayProvider;
	private final Skype2GmailClickActionProvider clickActionProvider;
	private final TrayFallbackWindowProvider trayFallbackWindow;

	@Inject
	public Skype2GmailTrayProviderImpl(
			SystemTrayProviderInterface systemTrayProvider,
			Skype2GmailMenuProvider trayMenuProvider, 
			Skype2GmailClickActionProvider clickActionProvider, 
			TrayFallbackWindowProvider trayFallbackWindow) 
	{
		this.systemTrayProvider = systemTrayProvider;
		this.trayMenuProvider = trayMenuProvider;
		this.clickActionProvider = clickActionProvider;
		this.trayFallbackWindow = trayFallbackWindow;
	}

	TrayIconAdapter trayIcon;
	@Override
	public void createTray() {
		if (systemTrayProvider.isSupported()) {
			if (trayIcon != null)
				return;
			createSystemTrayIcon();
			return;
		}
		trayFallbackWindow.display();
	}
	
	private void createSystemTrayIcon() {
		SystemTrayAdapter tray = systemTrayProvider.getSystemTray();
		PopupMenu popup = trayMenuProvider.getPopupMenu();

		URL skype2GmailImageUrl = getClass().getResource("/skype2gmail.svg");
		trayIcon = tray.createAndAddTrayIcon(skype2GmailImageUrl, "Skype2Gmail", popup);
		trayIcon.addActionListener(clickActionProvider.get());
	}
}
