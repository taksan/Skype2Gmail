package skype;

import skype2disk.SkypeHistoryMainModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.skype.connector.UnsupportedArchitectureException;

public class SkypeHistory {

	public static void main(String[] args) {
		try {
			Injector historyInjector = Guice.createInjector(new SkypeHistoryMainModule(args));
			SkypeHistoryMain skypeHistoryMain = historyInjector.getInstance(SkypeHistoryMain.class);
			skypeHistoryMain.run();
		}
		catch(IllegalStateException ex) {
			printExceptionMessageIfPossibleOrShowThePrintTheStackTrace(ex);
		}
	}

	private static void printExceptionMessageIfPossibleOrShowThePrintTheStackTrace(
			IllegalStateException ex) {
		Throwable cause = getRootCause(ex);
		if (cause != null && cause instanceof UnsupportedArchitectureException) {
			System.out.println(cause.getMessage());
		}
		else {
			System.out.println(cause.getClass().getName());
			ex.printStackTrace();
		}
	}

	private static Throwable getRootCause(Throwable ex) {
		while  (ex.getCause() != null) {
			ex = ex.getCause();
		}
		return ex;
	}
}
