package main;

import skype.commons.SkypeHistoryMain;
import skype2disk.SkypeHistoryMainModule;
import utils.ExceptionHandlingHelper;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class SkypeHistory {

	public static void main(String[] args) {
		try {
			Injector historyInjector = Guice.createInjector(new SkypeHistoryMainModule(args));
			SkypeHistoryMain skypeHistoryMain = historyInjector.getInstance(SkypeHistoryMain.class);
			skypeHistoryMain.run();
		}
		catch(IllegalStateException ex) {
			String errorMessage = ExceptionHandlingHelper.getExceptionMessageIfPossibleOrTheStackTrace(ex);
			System.err.println(errorMessage);
		}
	}
}
