package skype2disk;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import skype.EmptySkypeChat;
import skype.SkypeChat;
import skype.SkypeChatFactory;
import skype.SkypeStorage;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class FileSystemStorage implements SkypeStorage {

	private final File basedir;
	private final FileDumpContentParser fileDumpContentParser;

	@Inject
	public FileSystemStorage(SkypeChatFactory skypeChatFactory, 
			FileDumpContentParser fileDumpContentParser, 
			@Named("History Target") String baseDir) {
		this.fileDumpContentParser = fileDumpContentParser;
		basedir = new File(baseDir);
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
