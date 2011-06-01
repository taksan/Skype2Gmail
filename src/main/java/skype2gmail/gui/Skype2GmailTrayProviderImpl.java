package skype2gmail.gui;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import tray.SystemTrayAdapter;
import tray.SystemTrayProvider;
import tray.TrayIconAdapter;

public class Skype2GmailTrayProviderImpl implements Skype2GmailTrayProvider {

	@Override
	public SystemTray getTray() {
		final TrayIconAdapter trayIcon;

		if (SystemTray.isSupported()) {
			SystemTrayAdapter tray = new SystemTrayProvider().getSystemTray();
			PopupMenu popup = makeTrayPopupMenu();

			URL skype2GmailImageUrl = getClass().getResource("/skype2gmail.svg");
			trayIcon = tray.createAndAddTrayIcon(skype2GmailImageUrl, "Skype2Gmail", popup);
			
			ActionListener actionListener = makeDoubleClickAction(trayIcon);
			trayIcon.addActionListener(actionListener);
			trayIcon.setImageAutoSize(true);
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			return null;
		} 
		return null;
	}

	private ActionListener makeDoubleClickAction(final TrayIconAdapter trayIcon) {
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				trayIcon.displayMessage("Action Event",
						"An Action Event Has Been Performed...!",
						TrayIcon.MessageType.INFO);
			}
		};
		return actionListener;
	}

	private PopupMenu makeTrayPopupMenu() {
		ActionListener exitListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Exiting...");
				System.exit(0);
			}
		};

		PopupMenu popup = new PopupMenu();
		MenuItem defaultItem = new MenuItem("Exit");
		defaultItem.addActionListener(exitListener);
		popup.add(defaultItem);
		return popup;
	}
}
