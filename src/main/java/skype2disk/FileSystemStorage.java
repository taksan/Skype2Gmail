package skype2disk;

import java.io.File;

import skype.SkypeChat;
import skype.SkypeStorage;

public class FileSystemStorage implements SkypeStorage {

	private String basedir;

	public FileSystemStorage(String baseDir) {
		this.basedir = baseDir;
	}

	@Override
	public FileSystemStorageEntry newEntry(SkypeChat chat) {
		return new FileSystemStorageEntry(chat, new File(basedir));
	}

}
