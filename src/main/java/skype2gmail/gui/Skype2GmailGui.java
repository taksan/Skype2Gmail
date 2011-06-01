package skype2gmail.gui;


import com.google.inject.Inject;

public class Skype2GmailGui {
	
	private final Skype2GmailTrayProvider trayProvider;

	@Inject
	public Skype2GmailGui(Skype2GmailTrayProvider trayProvider) {
		this.trayProvider = trayProvider;
		
	}

	public void run() {
		trayProvider.getTray();
	}

}
