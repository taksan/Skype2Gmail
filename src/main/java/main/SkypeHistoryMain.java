package main;

import skype.commons.SkypeCliOptions;
import skype.commons.SkypeRecorder;
import skype2disk.Skype2DiskModule;
import skype2gmail.Skype2GmailModule;
import skype2gmail.gui.Skype2GmailGui;
import skype2gmail.gui.Skype2GmailGuiModule;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class SkypeHistoryMain {
	
	private final SkypeCliOptions historyCli;
	private final String[] args;
	@Inject
	public SkypeHistoryMain(SkypeCliOptions options, String [] args) {
		this.historyCli = options;
		this.args = args;
		
	}

	public void run() {
		if (historyCli.hasVersion()) {
			historyCli.printVersionAndExit();
			return;
		}
		if (historyCli.hasHelp()) {
			historyCli.printHelpAndExit();
			return;
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

	private void runSkypeModule(AbstractModule skype2StorageModule) {
		Injector injector = Guice.createInjector(skype2StorageModule);
		SkypeRecorder skypeRecorder = injector.getInstance(SkypeRecorder.class);
		skypeRecorder.record();
	}
}
