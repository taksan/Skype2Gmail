package skype2gmail.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.google.inject.Inject;

public class Skype2GmailClickActionProviderImpl implements Skype2GmailClickActionProvider {

	private final ConfigurationMenuProvider configMenuProvider;

	@Inject
	public Skype2GmailClickActionProviderImpl(
			ConfigurationMenuProvider configMenuProvider) {
		this.configMenuProvider = configMenuProvider;
	}

	@Override
	public ActionListener get() {
		return new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				configMenuProvider.display();
			}
		};
	}

}
