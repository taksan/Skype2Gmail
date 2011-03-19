package skype2gmail;

import gmail.GmailMessage;

import java.util.Date;

import com.google.inject.Inject;

import skype.SkypeChat;
import skype.SkypeChatWithBodyParserFactory;

public class GmailMessageChatParser {
	
	private final SkypeChatWithBodyParserFactory skypeChatWithBodyParserFactory;
	private String[] messageSignatures;

	@Inject
	public GmailMessageChatParser(SkypeChatWithBodyParserFactory skypeChatWithBodyParserFactory) {
		this.skypeChatWithBodyParserFactory = skypeChatWithBodyParserFactory;		
	}
	
	public SkypeChat parse(GmailMessage message) {
		messageSignatures = message.getMessagesSignatures();
		Date chatTime = message.getDate();
		return skypeChatWithBodyParserFactory.produce(
				message.getChatId(), 
				message.getTopic(),
				message.getUsers(), 
				message.getBodySignature(), 
				messageSignatures, 
				chatTime, 
				message.getBody());
	}

}
