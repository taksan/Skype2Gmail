package testutils;

import java.util.Date;

import skype.commons.SkypeChatMessage;
import skype.commons.SkypeChatMessageDataFactory;
import utils.DigestProvider;

public class MessageProducerUtil {

	public static SkypeChatMessage createMessage(String userId, String message,
			int month, int day, int hour, int minute, int second) {
		Date firstMessageTime = DateHelper.makeDate(2011, month, day, hour, minute, second);
		SkypeChatMessage firstMessage = MessageProducerUtil.skypeChatMessageFactory.produce(userId,
				userId.toUpperCase(), message, firstMessageTime);
		return firstMessage;
	}
	
	public static SkypeChatMessage createMessage(String userId, String message,
			String dateWithoutYearFormat_M_D_h_m_s) {
		
		String [] dateOn0_TimeOn1 = dateWithoutYearFormat_M_D_h_m_s.split(" ");
		String [] dayMonth = dateOn0_TimeOn1[0].split("/");
		int day = Integer.parseInt(dayMonth[0]);
		int month = Integer.parseInt(dayMonth[1])-1;
		
		String [] h_m_s = dateOn0_TimeOn1[1].split(":");
		int hour = Integer.parseInt(h_m_s[0]);
		int minute = Integer.parseInt(h_m_s[1]);
		int second = Integer.parseInt(h_m_s[2]);
		
		return createMessage(userId, message, month,day, hour, minute, second);
	}

	final static DigestProvider digestProvider = new DigestProvider();
	public final static SkypeChatMessageDataFactory skypeChatMessageFactory = new SkypeChatMessageDataFactory(digestProvider);

}
