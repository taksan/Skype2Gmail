package skype;

import skype2disk.Skype2DiskModule;
import skype2gmail.Skype2GmailModule;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class SkypeHistoryMain {
	
	private final SkypeHistoryCli historyCli;
	private final String[] args;
	@Inject
	public SkypeHistoryMain(SkypeHistoryCli options, String [] args) {
		this.historyCli = options;
		this.args = args;
		
	}

	public void run() {
		if (historyCli.hasVersion()) {
			historyCli.printVersionAndExit();
		}
		if (historyCli.hasHelp()) {
			historyCli.printHelpAndExit();
		}
		if (historyCli.isSyncToDisk()) {
			runSkypeModule(new Skype2DiskModule(args));
			return;
		}
		if (historyCli.isSyncToMail()) {
			runSkypeModule(new Skype2GmailModule());
			return;
		}
		runSkype2GmailGuiModule();
	}

	private void runSkype2GmailGuiModule() {
		Injector injector = Guice.createInjector(new Skype2GmailGuiModule());
		Skype2GmailGui gui = injector.getInstance(Skype2GmailGui.class);
		gui.run();
	}

	private void runSkypeModule(Skype2StorageModuleCommons skype2DiskModule) {
		Injector injector = Guice.createInjector(skype2DiskModule);
		SkypeRecorder skypeRecorder = injector.getInstance(SkypeRecorder.class);
		skypeRecorder.record();
	}
}
