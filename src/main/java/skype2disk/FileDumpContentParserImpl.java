package skype2disk;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import skype.SkypeChat;
import skype.SkypeChatDateFormat;
import skype.SkypeChatFactoryImpl;
import skype.SkypeChatMessage;
import skype.SkypeChatMessageDataFactory;
import skype.SkypeMessageDateFormat;
import skype.SkypeUser;
import skype.SkypeUserFactory;
import skype.TimeSortedMessages;
import skype.UsersSortedByUserId;

import com.google.inject.Inject;

public class FileDumpContentParserImpl implements FileDumpContentParser {

	private final SkypeChatMessageDataFactory skypeChatMessageDataFactory;
	private final SkypeChatFactoryImpl skypeChatFactoryImpl;
	private final SkypeChatDateFormat skypeChatDateFormat;
	private final SkypeMessageDateFormat skypeMessageDateFormat;
	private final SkypeUserFactory skypeUserFactory;

	@Inject
	public FileDumpContentParserImpl(SkypeChatFactoryImpl skypeChatFactoryImpl,
			SkypeChatMessageDataFactory skypeChatMessageData,
			SkypeChatDateFormat skypeChatDateFormat,
			SkypeMessageDateFormat skypeMessageDateFormat,
			SkypeUserFactory skypeUserFactory) 
	{
		this.skypeChatFactoryImpl = skypeChatFactoryImpl;
		this.skypeChatMessageDataFactory = skypeChatMessageData;
		this.skypeChatDateFormat = skypeChatDateFormat;
		this.skypeMessageDateFormat = skypeMessageDateFormat;
		this.skypeUserFactory = skypeUserFactory;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see skype2disk.FileDumpContentParser#parse(java.lang.String)
	 */
	@Override
	public SkypeChat parse(String fileContents) {

		String[] messageSections = fileContents.split(
				FileDumpContentBuilder.MESSAGE_TIME_FORMAT_FOR_PARSING, 2);

		final String[] headersSections = messageSections[0].split(
				"(?=Poster:.*)", 2);
		final String headerSection = headersSections[0];
		final String posters = headersSections[1];
		final String bodySection = messageSections[1];

		final Map<String, String> parsedContents = extractHeaders(headerSection);
		final UsersSortedByUserId userList = makeUserList(posters);

		final String[] messageSignatures = parsedContents
				.get("Messages signatures").replaceAll("[\\[\\]]", "")
				.split(",");

		final TimeSortedMessages messageList = makeMessageList(bodySection,
				userList, messageSignatures);

		final Date chatTime = makeChatTime(parsedContents.get("Chat Time"));
		final String chatId = parsedContents.get("Chat Id");
		final String topic = parsedContents.get("Chat topic");
		SkypeChat skypeChat = skypeChatFactoryImpl.produce(chatId, chatTime,
				topic, userList, messageList);

		final String parsedSignature = parsedContents
				.get("Chat Body Signature");
		final String recalculatedSignature = skypeChat.getBodySignature();
		if (!recalculatedSignature.equals(parsedSignature)) {
			throw new SkypeMessageParsingException(
					"Created chat does not match informed body signature (expected: %s, actual: %s)!",
					parsedSignature, recalculatedSignature);
		}

		return skypeChat;
	}

	private TimeSortedMessages makeMessageList(final String bodySection,
			UsersSortedByUserId userList, String[] messageSignatures) {
		TimeSortedMessages messageList = new TimeSortedMessages();
		String[] lines = bodySection
				.split(FileDumpContentBuilder.MESSAGE_TIME_FORMAT_FOR_PARSING);

		if (lines.length != messageSignatures.length) {
			throw new SkypeMessageParsingException(
					"Malformed message! Amount of messages doesn't match amount of signatures");
		}

		String previousPosterDisplay = null;
		int messagePosition = 0;
		for (String messageLine : lines) {
			final SkypeChatMessage skypeChatMessage = makeMessage(messageLine,
					userList, previousPosterDisplay);
			final String expectedSignature = messageSignatures[messagePosition];
			messagePosition++;
			if (!skypeChatMessage.getSignature().equals(expectedSignature)) {
				throw new SkypeMessageParsingException(
						"Message signature doesn't match original (expected: %s, actual: %s, message: %s)",
						expectedSignature, skypeChatMessage.getSignature(),
						messageLine);
			}
			previousPosterDisplay = skypeChatMessage.getSenderDisplayname();
			messageList.add(skypeChatMessage);
		}
		return messageList;
	}

	private SkypeChatMessage makeMessage(String line,
			UsersSortedByUserId userList, String previousPosterDisplay) {
		String[] lineParts = line.split("(: |\\.{3}( |$)|:$)", 2);
		String message;
		if (lineParts.length < 2) {
			message = "";
		} else {
			message = FileDumpContentBuilder.unescape(lineParts[1]);
		}

		final String[] timeAndUserInfo = lineParts[0].split(" (?=[^0-9])", 2);
		final String userDisplay;
		if (timeAndUserInfo.length == 2) {
			userDisplay = timeAndUserInfo[1];
		} else {
			userDisplay = previousPosterDisplay;
		}
		final SkypeUser skypeUser = userList.findByDisplayName(userDisplay);
		if (skypeUser == null) {
			throw new SkypeMessageParsingException(
					"User %s was found on chat, but was not among its posters!",
					userDisplay);
		}

		final String userId = skypeUser.getUserId();

		Date time = makeTimeFrom(timeAndUserInfo[0]);

		final SkypeChatMessage skypeChatMessage = skypeChatMessageDataFactory
				.produce(userId, userDisplay, message, time);
		return skypeChatMessage;
	}

	private Date makeTimeFrom(String messageTimeText) {
		return skypeMessageDateFormat.parse(messageTimeText);
	}

	private Date makeChatTime(String chatTime) {
		return skypeChatDateFormat.parse(chatTime);
	}

	private UsersSortedByUserId makeUserList(String userLines) {
		final String[] posters = userLines.split("\n");
		final Pattern pattern = Pattern
				.compile("Poster: id=([a-z0-9._]*); display=(.*)");

		UsersSortedByUserId skypeUserList = new UsersSortedByUserId();
		for (String posterLine : posters) {
			Matcher matcher = pattern.matcher(posterLine);
			if (!matcher.find()) {
				throw new SkypeMessageParsingException(
						"Invalid poster pattern found: %s", posterLine);
			}
			skypeUserList.add(
					skypeUserFactory.produce(
							matcher.group(1), 
							matcher.group(2))
					);
		}
		return skypeUserList;
	}

	private Map<String, String> extractHeaders(String headerContents) {
		Map<String, String> parsedContents = new LinkedHashMap<String, String>();
		final String[] lines = headerContents.split("\n");

		int i;
		for (i = 0; i < lines.length; i++) {
			final String line = lines[i];
			String[] lineParts = line.split(": ", 2);
			parsedContents.put(lineParts[0], lineParts[1]);
		}
		return parsedContents;
	}
}
