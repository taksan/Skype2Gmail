package skype;
import java.io.IOException;

import skype2disk.Skype2DiskModule;

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
		
		final String dumpTarget = getDumpTarget(args);
		Injector injector = Guice.createInjector(new Skype2DiskModule(dumpTarget));
		
		SkypeHistoryRecorder recorder = injector.getInstance(SkypeHistoryRecorder.class);
		
		recorder.record();
	}

	private static String getDumpTarget(String[] args) {
		final String dumpTarget;
		if (args.length > 0) {
			dumpTarget = args[0];
		}
		else {
			dumpTarget = null;
		}
		return dumpTarget;
	}

}
