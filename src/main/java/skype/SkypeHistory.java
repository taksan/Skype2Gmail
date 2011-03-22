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
		
		Injector injector = Guice.createInjector(new Skype2DiskModule(args));
		
		SkypeHistoryRecorder recorder = injector.getInstance(SkypeHistoryRecorder.class);
		
		recorder.record();
	}
}
