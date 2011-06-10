package skype2gmail.gui.mocks;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.commons.lang.NotImplementedException;

import skype2gmail.gui.Skype2GmailClickActionProvider;

public class Skype2GmailClickActionProviderMock implements Skype2GmailClickActionProvider {

	public boolean getWasInvoked = false;

	@Override
	public ActionListener get() {
		getWasInvoked = true;
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				throw new NotImplementedException();
			}
		};
	}

}
