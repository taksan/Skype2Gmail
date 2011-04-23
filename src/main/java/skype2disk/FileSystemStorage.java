package skype2disk;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import skype.ApplicationException;
import skype.SkypeChat;
import skype.SkypeChatFactory;
import skype.MessageProcessingException;
import skype.SkypeStorage;
import utils.LoggerProvider;
import utils.SimpleLoggerProvider;

import com.google.inject.Inject;

public class FileSystemStorage implements SkypeStorage {

	private Logger LOGGER;
	private final File historyDir;
	private final FileDumpContentParser fileDumpContentParser;
	private final SkypeChatFactory skypeChatFactory;
	private final LoggerProvider loggerProvider;

	@Inject
	public FileSystemStorage(SkypeChatFactory skypeChatFactory, 
			FileDumpContentParser fileDumpContentParser, 
			CustomHistoryDir baseDir,
			LoggerProvider loggerProvider) {
		this.skypeChatFactory = skypeChatFactory;
		this.fileDumpContentParser = fileDumpContentParser;
		this.loggerProvider = loggerProvider;
		historyDir = baseDir.getHistoryDir();
	}
	
	public void configureLogger() {
		if (LOGGER != null) {
			return;
		}
		LOGGER = loggerProvider.getLogger(FileSystemStorage.class);
		
		try {
			LOGGER.info("Will write messages to " + historyDir.getCanonicalPath());
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
	}
	
	public FileSystemStorage(SkypeChatFactory skypeChatFactory, 
			FileDumpContentParser fileDumpContentParser, 
			CustomHistoryDir baseDir) {
		this(skypeChatFactory,fileDumpContentParser,baseDir, new SimpleLoggerProvider());
	}

	@Override
	public FileSystemStorageEntry newEntry(SkypeChat chat) {
		configureLogger();
		
		return new FileSystemStorageEntry(chat, historyDir);
	}

	@Override
	public FileSystemStorageEntry retrievePreviousEntryFor(SkypeChat skypeChat) {
		configureLogger();
		
		final String entryName = FileSystemStorageEntry.getFilenameFor(skypeChat.getId());
		
		File previousFile = new File(historyDir, entryName);
		if (!previousFile.exists()) {
			return new FileSystemStorageEntry(skypeChatFactory.produceEmpty(), historyDir);
		}
		LOGGER.info("Found previous chat file: " + entryName);
		return new FileSystemStorageEntry(makeEntryFromFile(previousFile), historyDir);
	}

	private SkypeChat makeEntryFromFile(File previousFile) {
		try {
			final String fileContents = FileUtils.readFileToString(previousFile,"UTF-8");
			return fileDumpContentParser.parse(fileContents);
		} catch (IOException e) {
			throw new MessageProcessingException(e);
		}
	}

	@Override
	public void close() {
		// nothing to do here
	}

}
