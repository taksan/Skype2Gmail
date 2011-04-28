package skype2gmail;

import gmail.GmailMessageInterface;

import java.util.Date;

import com.google.inject.Inject;

import skype.SkypeChat;
import skype.SkypeChatWithBodyParserFactory;

public class GmailMessageChatParser {
	
	private final SkypeChatWithBodyParserFactory skypeChatWithBodyParserFactory;

	@Inject
	public GmailMessageChatParser(SkypeChatWithBodyParserFactory skypeChatWithBodyParserFactory) {
		this.skypeChatWithBodyParserFactory = skypeChatWithBodyParserFactory;		
	}
	
	public SkypeChat parse(GmailMessageInterface message) {
		Date chatTime = message.getDate();
		return skypeChatWithBodyParserFactory.produce(
				message.getChatId(), 
				message.getTopic(),
				message.getPosters(), 
				message.getBodySignature(), 
				message.getMessagesSignatures(), 
				chatTime, 
				message.getBody());
	}

}
