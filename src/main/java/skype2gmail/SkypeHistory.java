package skype2gmail;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.skype.Chat;
import com.skype.Skype;
import com.skype.SkypeException;


public class SkypeHistory {

	/**
	 * @param args
	 * @throws SkypeException 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws SkypeException, IOException, InterruptedException {
		File dumpdir = new File("/tmp/skypedump");
		
		dumpdir.mkdirs();
		Skype.getAllRecentChats();
		Thread.sleep(1000);
		
		Chat[] allRecentChats = Skype.getAllRecentChats();
        
        for(Chat chat: allRecentChats) {
            ChatMailBuilder chatMailBuilder = new ChatMailBuilder(new SkypeChatImpl(chat));
            
            File chatFile = new File(dumpdir, chat.getId());
            FileUtils.writeStringToFile(chatFile, chatMailBuilder.toChatText());
        }
	}
}
