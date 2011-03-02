package skype;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.skype.Chat;
import com.skype.Skype;
import com.skype.SkypeException;


public class SkypeHistory {

	private static final Logger LOGGER = Logger.getLogger(SkypeHistory.class);

	/**
	 * @param args
	 * @throws SkypeException 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws SkypeException, IOException, InterruptedException {
		final String dumpTarget;
		if (args.length > 0) {
			dumpTarget = args[0];
		}
		else {
			dumpTarget = System.getProperty("user.home")+"/.skypeChatHistory";
		}
		LOGGER.info("Dumping history to " + dumpTarget);
		
		File dumpdir = new File(dumpTarget);
		
		dumpdir.mkdirs();
		Skype.getAllRecentChats();
		Thread.sleep(1000);
		
		Chat[] allRecentChats = Skype.getAllRecentChats();
		LOGGER.info(String.format("Found %d chats.", allRecentChats.length));
        
        for(Chat chat: allRecentChats) {
            ChatMailBuilder chatMailBuilder = new ChatMailBuilder(new SkypeChatImpl(chat));
            
            File chatFile = new File(dumpdir, createChatDumpName(chat));
            FileUtils.writeStringToFile(chatFile, chatMailBuilder.toChatText()+"\n");
        }
        LOGGER.info("Done.");
	}

	private static String createChatDumpName(Chat chat) {
		return chat.getId().replace("#", "").replace("$", "$");
	}
}
