package testutils;

import java.util.Date;

import skype.SkypeChatImpl;
import skype.SkypeChatMessageData;
import skype.SkypeUserImpl;
import skype.TimeSortedMessages;
import skype.UsersSortedByUserId;
import skype.mocks.SkypeChatMock;
import utils.DigestProvider;

public class SkypeChatHelper {

	public static SkypeChatMock createSkypeTestEnvironment() {
		final String chatId = "#camaron.goo/$goofoo;81ef2618fc9a6343";
		final Date chatDate = getDefaultChatTime();
		final String[] members = new String[] { "camaron.goo", "goofoo" };
		final SkypeChatMock chat = new SkypeChatMock(chatId, chatDate, "FOO", members);

		chat.addMockMessage("2011/03/21 15:14:18", "goofoo", "Goo Foo",
				"what's up")
				.addMockMessage("2011/03/21 15:18:16", "camaron.goo",
						"Camaron", "so far so good")
				.addMockMessage("2011/03/21 15:14:24", "goofoo", "Goo Foo",
						"doing fine??");
		return chat;
	}
	final static private DigestProvider digestProvider = new DigestProvider();
	
	public static SkypeChatImpl createSkypeImplForTest(String chatId, String topic, String [] users)
	{
		if (users.length < 2) {
			throw new IllegalArgumentException("You must provide at least two users to create the mock instance.");
		}
		final Date chatTime = getDefaultChatTime();
		
		final UsersSortedByUserId userIds = makeUserList(users);
		
		final TimeSortedMessages timeSortedMessages = new TimeSortedMessages();
		
		timeSortedMessages.add(createSkypeMessageDataForTest(users[0],"howdy"));
		timeSortedMessages.add(createSkypeMessageDataForTest(users[1],"hiiya"));
		
		return new SkypeChatImpl(digestProvider, chatId, chatTime, topic, userIds, timeSortedMessages);
	}

	public static UsersSortedByUserId makeUserList(String[] users) {
		final UsersSortedByUserId userIds = new UsersSortedByUserId();
		for(String user : users) {
			userIds.add(new SkypeUserImpl(user, user.toUpperCase()));
		}
		return userIds;
	}
	
	private static int minuteCounter = 1;
	public static SkypeChatMessageData createSkypeMessageDataForTest(String fromUser, String message){
		final Date nextMessageTime = getNextMessageTime();
		return new SkypeChatMessageData(
				digestProvider, fromUser, fromUser.toUpperCase(), 
				message, 
				nextMessageTime);
	}

	private static Date getNextMessageTime() {
		Date nextMessageTime = DateHelper.makeDate(2011, 02, 21, 15, minuteCounter , 00);
		minuteCounter++;
		return nextMessageTime;
	}

	private static Date getDefaultChatTime() {
		return DateHelper.makeDate(2011, 02, 21, 15, 00, 00);
	}
}
