package testutils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

public class IOHelper {

	public static synchronized File createTempDirOrCry() {
		try {
			File baseDir = File.createTempFile("simpleTestRootDir", null);
			baseDir.delete();
			baseDir.mkdir();
			return baseDir;
		} catch(IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static String getSortedFilesAsString(File tempTarget) {
		String[] list = tempTarget.list();
		Arrays.sort(list);
		return StringUtils.join(list,"\n");
	}
}
