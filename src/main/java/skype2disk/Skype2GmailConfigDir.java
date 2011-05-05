package skype2disk;

import java.io.File;
import java.io.IOException;

import skype.ApplicationException;
import skype.BasePath;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class Skype2GmailConfigDir {
	
	private final BasePath basePath;
	private File configDir;

	@Inject
	public Skype2GmailConfigDir(BasePath basePath)
	{
		this.basePath = basePath;
	}

	public String getFileNameUnder(String sub) {
		try {
			return getFileUnder(sub).getCanonicalPath();
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
	}

	public File getFileUnder(String sub) {
		return new File(this.getDirectory(), sub);
	}

	public File getConfigFile() {
		return new File(this.getDirectory(), "config");
	}
	
	public File getDirectory() {
		if (configDir == null)
			configDir = new File(basePath.getPath(), ".skype2gmail");
		return configDir;
	}
}
