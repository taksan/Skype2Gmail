package skype2gmail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import mail.RequiredConfigurationMissingException;

import org.apache.commons.io.FileUtils;

import skype.ApplicationException;
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
			final File file = getConfigFile(configDir);
			if (!file.exists()) {
				FileUtils.touch(file);
			}
			config = new Properties();
			config.load(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			throw new ApplicationException(e);
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
	}

	private File getConfigFile(Skype2GmailConfigDir configDir) {
		return new File(configDir.getDirectory(), "config");
	}

	public String getProperty(String key, Boolean required) {
		if (config == null) {
			readConfiguration(configDir);
		}
		String value = config.getProperty(key);
		if (value == null && required) {
			File configFile = getConfigFile(configDir);
			String fmtMsg = String.format("Required configuration %s missing in config file %s", 
					key, configFile.getAbsolutePath());
			throw new RequiredConfigurationMissingException(fmtMsg);
		}
		return value;
	}
}
