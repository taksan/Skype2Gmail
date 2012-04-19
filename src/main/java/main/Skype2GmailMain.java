package main;

import skype2disk.SkypeHistoryMainModule;
import utils.ExceptionHandlingHelper;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Skype2GmailMain {

	public static void main(String[] args) {
		try {
			Injector historyInjector = Guice.createInjector(new SkypeHistoryMainModule(args));
			SkypeHistoryMainOptions skypeHistoryMain = historyInjector.getInstance(SkypeHistoryMainOptions.class);
			skypeHistoryMain.run();
		}
		catch(IllegalStateException ex) {
			String errorMessage = ExceptionHandlingHelper.getExceptionMessageIfPossibleOrTheStackTrace(ex);
			System.err.println(errorMessage);
		}
	}
}
