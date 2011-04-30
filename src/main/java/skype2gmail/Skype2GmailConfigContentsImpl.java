package skype2gmail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import skype.ApplicationException;
import skype2disk.Skype2GmailConfigDir;
import utils.Maybe;

import com.google.inject.Inject;

public class Skype2GmailConfigContentsImpl implements Skype2GmailConfigContents {
	private Properties config;
	private final Skype2GmailConfigDir configDir;

	@Inject
	public Skype2GmailConfigContentsImpl(Skype2GmailConfigDir configDir) {
		this.configDir = configDir;
	}

	public Maybe<String> getProperty(String key) {
		if (config == null) {
			readConfiguration();
		}
		String property = config.getProperty(key);
		return new Maybe<String>(property);
	}

	@Override
	public void setProperty(String key, String value) {
		config.setProperty(key, value);
		save();
	}

	private void save() {
		final File file = configDir.getConfigFile();
		try {
			config.store(new FileOutputStream(file), "Skype2Gmail configuration");
		} catch (FileNotFoundException e) {
			throw new ApplicationException(e);
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
	}

	private void readConfiguration() {
		try {
			final File file = configDir.getConfigFile();
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
}
