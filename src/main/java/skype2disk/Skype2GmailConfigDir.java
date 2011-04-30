package skype2disk;

import java.io.File;
import java.io.IOException;

import skype.ApplicationException;

public class Skype2GmailConfigDir {
	
	private final File configDir;

	public Skype2GmailConfigDir()
	{
		configDir = new File(System.getProperty("user.home"), ".skype2gmail");
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
