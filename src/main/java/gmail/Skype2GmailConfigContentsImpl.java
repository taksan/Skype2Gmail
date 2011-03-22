package gmail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import skype2disk.Skype2GmailConfigDir;

import com.google.inject.Inject;

public class Skype2GmailConfigContentsImpl implements Skype2GmailConfigContents {
	private Properties config;
	private final Skype2GmailConfigDir configDir;

	@Inject
	public Skype2GmailConfigContentsImpl(Skype2GmailConfigDir configDir) {
		this.configDir = configDir;
	}

	private void readConfiguration(Skype2GmailConfigDir configDir) {
		try {
			final File file = new File(configDir.getDirectory(), "config");
			if (!file.exists()) {
				FileUtils.touch(file);
			}
			config = new Properties();
			config.load(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String getProperty(String key) {
		if (config == null) {
			readConfiguration(configDir);
		}
		return config.getProperty(key);
	}
}
