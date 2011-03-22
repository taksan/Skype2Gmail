package skype2disk;

import java.io.File;

public class Skype2GmailConfigDir {
	
	private final File configDir;

	public Skype2GmailConfigDir()
	{
		configDir = new File(System.getProperty("user.home", ".skype2gmail"));
	}

	public String getSubdirectory(String sub) {
		return new File(configDir, sub).getAbsolutePath();
	}

}
