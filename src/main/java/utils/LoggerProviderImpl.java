package utils;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.RollingFileAppender;

import skype2disk.Skype2GmailConfigDir;

import com.google.inject.Inject;

public class LoggerProviderImpl implements LoggerProvider {
	private final Skype2GmailConfigDir configDir;
	private boolean setupIsDone;

	@Inject
	public LoggerProviderImpl(Skype2GmailConfigDir configDir) {
		this.configDir = configDir;
	}

	private RollingFileAppender getHomeAppender() {
		RollingFileAppender rollingFileAppender = new RollingFileAppender();
		rollingFileAppender.setMaxFileSize("10M");
		rollingFileAppender.setFile(getHomeLoggerPath());
		return rollingFileAppender;
	}

	private String getHomeLoggerPath() {
		try {
			File logDirectory = configDir.getFileUnder("logs");
			logDirectory.mkdirs();
			File logFile = new File(logDirectory, "sync.log");
			return logFile.getCanonicalPath();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Logger getLogger(Class<?> forClass) {
		if (!setupIsDone) {
			setupLogger();
			setupIsDone = true;
		}
		return Logger.getLogger(forClass);
	}

	public void setupLogger() {
		Logger.getRootLogger().addAppender(getHomeAppender());
	}
}
