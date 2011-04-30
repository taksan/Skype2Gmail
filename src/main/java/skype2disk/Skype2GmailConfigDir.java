package skype2disk;

import java.io.File;
import java.io.IOException;

import skype.ApplicationException;
import skype.BasePath;

import com.google.inject.Inject;

public class Skype2GmailConfigDir {
	
	private final File configDir;

	@Inject
	public Skype2GmailConfigDir(BasePath basePath)
	{
		configDir = new File(basePath.getPath(), ".skype2gmail");
	}

	public String getFileNameUnder(String sub) {
		try {
			return getFileUnder(sub).getCanonicalPath();
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
	}

	public File getFileUnder(String sub) {
		return new File(configDir, sub);
	}
	
	public File getDirectory() {
		return configDir;
	}

	public File getConfigFile() {
		return new File(this.getDirectory(), "config");
	}
}
