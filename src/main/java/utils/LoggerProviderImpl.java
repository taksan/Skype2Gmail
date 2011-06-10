package utils;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.SimpleLayout;

import skype.exceptions.ApplicationException;
import skype2disk.Skype2GmailConfigDir;
import skype2gmail.Skype2GmailConfigContents;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class LoggerProviderImpl implements LoggerProvider {
	private final Skype2GmailConfigDir configDir;
	private boolean setupIsDone;
	private final Skype2GmailConfigContents configContents;
	private ConsoleAppender consoleAppender;
	
	@Inject
	public LoggerProviderImpl(Skype2GmailConfigDir configDir, Skype2GmailConfigContents configContents) {
		this.configDir = configDir;
		this.configContents = configContents;
	}

	@Override
	public Logger getLogger(Class<?> forClass) {
		return this.getLogger(forClass.toString());
	}
	

	@Override
	public Logger getPriorityLogger(Class<?> forClass) {
		Logger priorityLogger = this.getLogger(forClass);
		if (!priorityLogger.isAttached(getConsoleLogger())) {
			priorityLogger.addAppender(getConsoleLogger());
			priorityLogger.setAdditivity(false);
			priorityLogger.setLevel(Level.INFO);
		}
		
		return priorityLogger;
	}
	

	@Override
	public Logger getLogger(String loggerName) {
		if (!setupIsDone) {
			setupLogger();
			setupIsDone = true;
		}
		return Logger.getLogger(loggerName);
	}
	

	private void setupLogger() {
		Logger.getRootLogger().addAppender(getHomeAppender());
		if (configContents.isOutputVerbose()) {
			Logger.getRootLogger().addAppender(getConsoleLogger());
			Logger logger = Logger.getLogger(getClass());
			logger.info("Verbose output enabled. To disable edit: ");
			logger.info("   " +configDir.getConfigFile().getAbsolutePath());
			logger.info("and add: verbosity=quiet");
		}
	}

	private ConsoleAppender getConsoleLogger() {
		if (consoleAppender == null)
			consoleAppender = new ConsoleAppender(new SimpleLayout());
		return consoleAppender;
	}	

	private RollingFileAppender getHomeAppender() {
		RollingFileAppender rollingFileAppender = new RollingFileAppender();
		rollingFileAppender.setLayout(new SimpleLayout());
		rollingFileAppender.setName("Skype2GmailHomeLogger");
		rollingFileAppender.setMaxFileSize("10MB");
		rollingFileAppender.setFile(getHomeLoggerPath());
		rollingFileAppender.setAppend(true);
		rollingFileAppender.activateOptions();
		
		return rollingFileAppender;
	}

	private String getHomeLoggerPath() {
		try {
			File logDirectory = configDir.getFileUnder("logs");
			logDirectory.mkdirs();
			File logFile = new File(logDirectory, "sync.log");
			return logFile.getCanonicalPath();
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
	}

}
