package skype2disk;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import skype.EmptySkypeChat;
import skype.SkypeChat;
import skype.SkypeChatFactory;
import skype.SkypeStorage;

import com.google.inject.Inject;

public class FileSystemStorage implements SkypeStorage {

	private static final Logger LOGGER = Logger.getLogger(FileSystemStorage.class);
	private final File basedir;
	private final FileDumpContentParser fileDumpContentParser;

	@Inject
	public FileSystemStorage(SkypeChatFactory skypeChatFactory, 
			FileDumpContentParser fileDumpContentParser, 
			CustomHistoryDir baseDir) {
		this.fileDumpContentParser = fileDumpContentParser;
		basedir = baseDir.getHistoryDir();
	}

	@Override
	public FileSystemStorageEntry newEntry(SkypeChat chat) {
		return new FileSystemStorageEntry(chat, basedir);
	}

	@Override
	public FileSystemStorageEntry retrievePreviousEntryFor(SkypeChat skypeChat) {
		final String entryName = FileSystemStorageEntry.getFilenameFor(skypeChat.getId());
		
		File previousFile = new File(basedir, entryName);
		if (!previousFile.exists()) {
			return new FileSystemStorageEntry(new EmptySkypeChat(), basedir);
		}
		LOGGER.info("Found previous chat file: " + entryName);
		return new FileSystemStorageEntry(makeEntryFromFile(previousFile), basedir);
	}

	private SkypeChat makeEntryFromFile(File previousFile) {
		try {
			final String fileContents = FileUtils.readFileToString(previousFile);
			return fileDumpContentParser.parse(fileContents);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

}
