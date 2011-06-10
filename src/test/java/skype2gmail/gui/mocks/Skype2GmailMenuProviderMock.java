package skype2gmail.gui.mocks;

import java.awt.PopupMenu;

import skype2gmail.gui.Skype2GmailMenuProvider;

public class Skype2GmailMenuProviderMock implements Skype2GmailMenuProvider {
	PopupMenu popupMenu = new PopupMenu();
	public boolean getMenuWasInvoked = false;

	@Override
	public PopupMenu getPopupMenu() {
		getMenuWasInvoked = true;
		return popupMenu;
	}

}
