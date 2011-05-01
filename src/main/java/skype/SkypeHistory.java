package skype;

import java.io.IOException;
import java.util.Arrays;

import skype2disk.Skype2DiskModule;
import skype2gmail.Skype2GmailModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.skype.SkypeException;
import com.skype.connector.UnsupportedArchitectureException;

public class SkypeHistory {

	/**
	 * @param args
	 * @throws SkypeException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) {
		try {
			if (args.length > 0 && args[0].equals("--mail")) {
				skype2gmail();
			} else
			if (args.length > 0 && args[0].equals("--disk")) {
				final String [] argsForDisk;
				if (args.length == 0) {
					argsForDisk = new String[0];
				}
				else {
					argsForDisk = Arrays.copyOfRange(args, 1, args.length);
				}
				skype2disk(argsForDisk);
			}
			else {
				printHelpAndExit();
			}
		}
		catch(IllegalStateException ex) {
			Throwable cause = getRootCause(ex);
			if (cause != null && cause instanceof UnsupportedArchitectureException) {
				System.out.println(cause.getMessage());
			}
			else {
				System.out.println(cause.getClass().getName());
				ex.printStackTrace();
			}
		}
	}

	private static void printHelpAndExit() {
		System.out.println("Usage:");
		System.out.println("	--mail : synchronize with a gmail account");
		System.out.println("	--disk : synchornize with local disk (in the .skype2gmail directory)");
		System.exit(1);
	}

	private static Throwable getRootCause(Throwable ex) {
		while  (ex.getCause() != null) {
			ex = ex.getCause();
		}
		return ex;
	}

	private static void skype2gmail() {
		Injector injector = Guice.createInjector(new Skype2GmailModule());

		SkypeHistoryRecorder recorder = injector.getInstance(SkypeHistoryRecorder.class);

		recorder.record();
	}

	private static void skype2disk(String[] args) {
		Injector injector = Guice.createInjector(new Skype2DiskModule(args));

		SkypeHistoryRecorder recorder = injector
				.getInstance(SkypeHistoryRecorder.class);

		recorder.record();
	}
}
