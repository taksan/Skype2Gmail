package skype2gmail;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.io.FileUtils;

import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.Skype;
import com.skype.SkypeException;


public class SkypeHistory {

	/**
	 * @param args
	 * @throws SkypeException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws SkypeException, IOException {
		File dumpdir = new File("/tmp/skypedump");
		
		dumpdir.mkdirs();
		
		Chat[] allRecentChats = Skype.getAllRecentChats();
        
        for(Chat chat: allRecentChats) {
        	StringBuilder chatString = new StringBuilder();
            ChatMessage[] allChatMessages = chat.getAllChatMessages();
            chatString.append("Chat ID: " + chat.getId());
            chatString.append(" at " + chat.getTime());
            chatString.append("\n");
            
            System.out.println(chatString);
            
            sortMessagesByTime(allChatMessages);
			
            concatenateMessages(chatString, allChatMessages);
            
            File chatFile = new File(dumpdir, chat.getId());
            FileUtils.writeStringToFile(chatFile, chatString.toString());
        }
	}

	private static void concatenateMessages(StringBuilder chatString,
			ChatMessage[] allChatMessages) throws SkypeException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("H:m:s");
		for(ChatMessage chatMessage: allChatMessages) {
			chatString.append("[" + dateFormat.format(chatMessage.getTime()) + "]");
			chatString.append(chatMessage.getSenderDisplayName());
			chatString.append("(" + chatMessage.getSenderId() + ")");
			chatString.append("\n");
			
			chatString.append("(msg id:"+chatMessage.getId()+")");
			chatString.append(chatMessage.getContent());
			chatString.append("\n");
		}
	}

	private static void sortMessagesByTime(ChatMessage[] allChatMessages) {
		Arrays.sort(allChatMessages, new Comparator<ChatMessage>() {

			@Override
			public int compare(ChatMessage o1, ChatMessage o2) {
				try {
					return o1.getTime().compareTo(o2.getTime());
				} catch (SkypeException e) {
					throw new IllegalStateException(e);
				}
			}
		});
	}

}
