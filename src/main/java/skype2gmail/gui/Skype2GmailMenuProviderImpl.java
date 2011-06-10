package skype2gmail.gui;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Skype2GmailMenuProviderImpl implements Skype2GmailMenuProvider{
	PopupMenu popupMenu = new PopupMenu();

	@Override
	public PopupMenu getPopupMenu() {
		popupMenu.add(makeExitOption());
		return popupMenu;
	}

	private MenuItem makeExitOption() {
		MenuItem exitItem = new MenuItem("Exit");
		exitItem.addActionListener(makeExitAction());
		return exitItem;
	}

	private ActionListener makeExitAction() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		};
	}
}
