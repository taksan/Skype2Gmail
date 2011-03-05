package skype2disk;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import skype.SkypeChatImpl;
import skype.SkypeHistoryRecorder;

import com.skype.Chat;
import com.skype.Skype;

public class Skype2Disk implements SkypeHistoryRecorder {
	
	private static final Logger LOGGER = Logger.getLogger(Skype2Disk.class);
	private final String dumpTarget;
	
	public Skype2Disk(String dumpTarget){
		if (dumpTarget == null) {
			dumpTarget = getDefaultDumpDirectory();
		}
		this.dumpTarget = dumpTarget;
		
	}

	private String getDefaultDumpDirectory() {
		return System.getProperty("user.home")+"/.skypeChatHistory";
	}

	@Override
	public void record() {
		LOGGER.info("Dumping history to " + dumpTarget);

		File dumpdir = new File(dumpTarget);

		dumpdir.mkdirs();
		Chat[] allRecentChats;
		try {
			Skype.getAllRecentChats();
			Thread.sleep(100);
			allRecentChats = Skype.getAllRecentChats();
		} catch (Exception e1) {
			throw new RuntimeException(e1);
		}
		LOGGER.info(String.format("Found %d chats.", allRecentChats.length));

		for (Chat chat : allRecentChats) {
			FileDumpBuilder chatMailBuilder = new FileDumpBuilder(
					new SkypeChatImpl(chat));

			File chatFile = new File(dumpdir, createChatDumpName(chat));
			try {
				FileUtils.writeStringToFile(chatFile, chatMailBuilder.toChatText()+ "\n");
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}
		LOGGER.info("Done.");
	}


	private String createChatDumpName(Chat chat) {
		return chat.getId().replaceAll("[#$;]", "");
	}
}
