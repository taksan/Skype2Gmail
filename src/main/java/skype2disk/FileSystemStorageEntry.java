package skype2disk;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import skype.SkypeChat;
import skype.StorageEntry;

public class FileSystemStorageEntry implements StorageEntry {

	private final File entryFile;
	private final StringBuilder entryContent;
	private Date lastModificationTime;

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
			setLastModificationTimeIfNeeded();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void setLastModificationTimeIfNeeded() {
		if (this.lastModificationTime != null)
			this.entryFile.setLastModified(this.lastModificationTime.getTime());
	}
	
	@Override
	public void setLastModificationTime(Date time) {
		this.lastModificationTime = time;
	}

	File getFile() {
		return this.entryFile;
	}


	private String getFilenameFor(SkypeChat chat) {
		return chat.getId().replaceAll("[#$;]", "");
	}
}
