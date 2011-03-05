package skype;
import java.io.IOException;

import skype2disk.Skype2Disk;

import com.skype.SkypeException;


public class SkypeHistory {

	/**
	 * @param args
	 * @throws SkypeException 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) {
		final String dumpTarget;
		if (args.length > 0) {
			dumpTarget = args[0];
		}
		else {
			dumpTarget = null;
		}
		new Skype2Disk(dumpTarget).record();
	}

}
