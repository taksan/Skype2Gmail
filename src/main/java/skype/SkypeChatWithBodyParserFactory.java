package skype;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.inject.Inject;

import skype2disk.MessageBodyParser;
import skype2disk.MessageBodyParserFactory;
import skype2disk.SkypeMessageParsingException;

public class SkypeChatWithBodyParserFactory {
	private final SkypeChatFactoryImpl skypeChatFactoryImpl;
	private final MessageBodyParserFactory messageBodyParserFactory;
	private final SkypeUserFactory skypeUserFactory;
	
	@Inject
	public SkypeChatWithBodyParserFactory(
			SkypeChatFactoryImpl skypeChatFactoryImpl,
			MessageBodyParserFactory messageBodyParserFactory,
			SkypeUserFactory skypeUserFactory) {
		this.skypeChatFactoryImpl = skypeChatFactoryImpl;
		this.messageBodyParserFactory = messageBodyParserFactory;
		this.skypeUserFactory = skypeUserFactory;
	}

	public SkypeChat produce(
			final String chatId,
			final String topic,
			final String [] posters, 
			final String bodySignature,
			final String[] messageSignatures, 
			final Date chatTime,
			final String bodySection) {
		final UsersSortedByUserId userList = makeUserList(posters);
		
		MessageBodyParser messageBodyParser = messageBodyParserFactory.produce(userList, messageSignatures);
		
		final TimeSortedMessages messageList = messageBodyParser.parse(bodySection);
		final SkypeChat skypeChat = skypeChatFactoryImpl.produce(chatId, chatTime, topic, userList, messageList);
		
		final String recalculatedSignature = skypeChat.getBodySignature();
		if (!recalculatedSignature.equals(bodySignature)) {
			throw new SkypeMessageParsingException(
					"Created chat does not match informed body signature (expected: %s, actual: %s)!",
					bodySignature, recalculatedSignature);
		}
		return skypeChat;
	}
	
	private UsersSortedByUserId makeUserList(final String[] posters) {
		final Pattern pattern = Pattern
				.compile("Poster: id=([a-z0-9._]*); display=(.*)");

		UsersSortedByUserId skypeUserList = new UsersSortedByUserId();
		for (String posterLine : posters) {
			Matcher matcher = pattern.matcher(posterLine);
			if (!matcher.find()) {
				throw new SkypeMessageParsingException("Invalid poster pattern found: %s", posterLine);
			}
			skypeUserList.add(
					skypeUserFactory.produce(
							matcher.group(1), 
							matcher.group(2))
					);
		}
		return skypeUserList;
	}
}
