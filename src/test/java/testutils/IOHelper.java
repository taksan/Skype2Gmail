package testutils;

import java.io.File;
import java.io.IOException;

public class IOHelper {

	public static File createTempDirOrCry() {
		try {
			File baseDir = File.createTempFile("simpleTestRootDir", null);
			baseDir.delete();
			baseDir.mkdir();
			return baseDir;
		} catch(IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}
