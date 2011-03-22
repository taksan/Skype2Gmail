package skype;

import java.io.IOException;
import java.util.Arrays;

import skype2disk.Skype2DiskModule;
import skype2gmail.Skype2GmailModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.skype.SkypeException;

public class SkypeHistory {

	/**
	 * @param args
	 * @throws SkypeException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) {

		if (args.length > 0 && args[0].equals("disk")) {
			String [] argsForDisk = Arrays.copyOfRange(args, 1, args.length);
			skype2disk(argsForDisk);
		} else {
			skype2gmail();
		}
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
