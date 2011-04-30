package skype2gmail;


import java.util.Date;

import mail.SkypeMailMessage;

import com.google.inject.Inject;

import skype.SkypeChat;
import skype.SkypeChatWithBodyParserFactory;

public class MailMessageChatParser {
	
	private final SkypeChatWithBodyParserFactory skypeChatWithBodyParserFactory;

	@Inject
	public MailMessageChatParser(SkypeChatWithBodyParserFactory skypeChatWithBodyParserFactory) {
		this.skypeChatWithBodyParserFactory = skypeChatWithBodyParserFactory;		
	}
	
	public SkypeChat parse(SkypeMailMessage message) {
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
