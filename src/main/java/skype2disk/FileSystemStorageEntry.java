package skype2disk;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import skype.ChatContentBuilder;
import skype.MessageProcessingException;
import skype.SkypeChat;
import skype.SkypeChatSetter;
import skype.StorageEntry;

public class FileSystemStorageEntry implements StorageEntry {
	private final File entryFile;
	private Date lastModificationTime;
	private final SkypeChat chat;

	public FileSystemStorageEntry(SkypeChat chat, File basedir) {
		this.chat = chat;
		String preparedFileName = getFilenameFor(chat.getId());
		this.entryFile = new File(basedir, preparedFileName);
	}

	@Override
	public void store(SkypeChatSetter chat) {
	}
	
	@Override
	public SkypeChat getChat() {
		return this.chat;
	}

	@Override
	public void save() {
		try {
			ChatContentBuilder contentBuilder = new FileDumpContentBuilder(chat);
			FileUtils.writeStringToFile(entryFile, contentBuilder.getContent(),"UTF-8");
			setLastModificationTimeIfNeeded();
		} catch (IOException e) {
			throw new MessageProcessingException(e);
		}
	}

	@Override
	public void setLastModificationTime(Date time) {
		this.lastModificationTime = time;
	}


	public static String getFilenameFor(String chatId) {
		return chatId.replaceAll("[#$;]", "");
	}
	
	File getGeneratedFile() {
		return this.entryFile;
	}

	private void setLastModificationTimeIfNeeded() {
		if (this.lastModificationTime != null)
			this.entryFile.setLastModified(this.lastModificationTime.getTime());
	}
	
}
