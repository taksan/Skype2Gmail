package skype2disk;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import skype.SkypeChat;
import skype.StorageEntry;

public class FileSystemStorageEntry implements StorageEntry {

	private final File entryFile;
	private final StringBuilder entryContent;

	public FileSystemStorageEntry(SkypeChat chat, File basedir) {
		String preparedFileName = getFilenameFor(chat);
		this.entryFile = new File(basedir, preparedFileName);
		this.entryContent = new StringBuilder();
	}

	@Override
	public void write(String content) {
		entryContent.append(content);
	}

	@Override
	public void save() {
		try {
			FileUtils.writeStringToFile(entryFile, entryContent.toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	File getFile() {
		return this.entryFile;
	}


	private String getFilenameFor(SkypeChat chat) {
		return chat.getId().replaceAll("[#$;]", "");
	}
}
