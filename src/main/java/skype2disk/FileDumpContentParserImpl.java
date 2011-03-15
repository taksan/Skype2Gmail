package skype2disk;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import skype.SkypeChat;
import skype.SkypeChatFactoryImpl;
import skype.SkypeChatMessage;
import skype.SkypeChatMessageDataFactory;
import skype.SkypeUser;
import skype.SkypeUserImpl;
import skype.TimeSortedMessages;
import skype.UsersSortedByUserId;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class FileDumpContentParserImpl implements FileDumpContentParser {

	private final SkypeChatMessageDataFactory skypeChatMessageDataFactory;
	private final SkypeChatFactoryImpl skypeChatFactoryImpl;
	private final DateFormat skypeChatDateFormat;
	private final DateFormat skypeMessageDateFormat;

	@Inject
	public FileDumpContentParserImpl(
			SkypeChatFactoryImpl skypeChatFactoryImpl,
			SkypeChatMessageDataFactory skypeChatMessageData,
			@Named("Skype Chat Date Format") DateFormat skypeChatDateFormat,
			@Named("Skype Message Date Format") DateFormat skypeMessageDateFormat) {
		this.skypeChatFactoryImpl = skypeChatFactoryImpl;
		this.skypeChatMessageDataFactory = skypeChatMessageData;
		this.skypeChatDateFormat = skypeChatDateFormat;
		this.skypeMessageDateFormat = skypeMessageDateFormat;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see skype2disk.FileDumpContentParser#parse(java.lang.String)
	 */
	@Override
	public SkypeChat parse(String fileContents) {

		final String messageTimePattern = "\n(?=\\[\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}])";
		String[] messageSections = fileContents.split(messageTimePattern, 2);

		final String[] headersSections = messageSections[0].split("(?=Poster:.*)", 2);
		final String headerSection = headersSections[0];
		final String posters = headersSections[1];
		final String bodySection = messageSections[1];

		final Map<String, String> parsedContents = extractHeaders(headerSection);
		final UsersSortedByUserId userList = makeUserList(posters);

		final TimeSortedMessages messageList = makeMessageList(messageTimePattern, bodySection, userList);

		final Date chatTime = makeChatTime(parsedContents.get("Chat Time"));
		final String chatId = parsedContents.get("Chat Id");
		final String topic = parsedContents.get("Chat topic");
		SkypeChat skypeChat = skypeChatFactoryImpl.produce(chatId, chatTime,
				topic, userList, messageList);

		final String parsedSignature = parsedContents.get("Chat Body Signature");
		final String recalculatedSignature = skypeChat.getBodySignature();
		if (!recalculatedSignature.equals(parsedSignature)) {
			throw new IllegalStateException(
					String.format(
							"Created chat does not match informed body signature (expected: %s, actual: %s)!",
							parsedSignature, recalculatedSignature));
		}

		return skypeChat;
	}

	private TimeSortedMessages makeMessageList(final String messageTimePattern,
			final String bodySection, 
			UsersSortedByUserId userList) {
		TimeSortedMessages messageList = new TimeSortedMessages();
		String[] lines = bodySection.split(messageTimePattern);
		String previousPosterDisplay = null;
		for (String messageLine : lines) {
			final SkypeChatMessage skypeChatMessage = makeMessage(messageLine, userList, previousPosterDisplay);
			previousPosterDisplay = skypeChatMessage.getSenderDisplayname();
			messageList.add(skypeChatMessage);
		}
		return messageList;
	}

	private SkypeChatMessage makeMessage(String line,
			UsersSortedByUserId userList, String previousPosterDisplay) {
		String[] lineParts = line.split("(: |\\.{3} )");
		String message = lineParts[1];

		final String[] timeAndUserInfo = lineParts[0].split(" (?=[^0-9])", 2);
		final String userDisplay;
		if (timeAndUserInfo.length == 2) {
			userDisplay = timeAndUserInfo[1];
		}
		else {
			userDisplay = previousPosterDisplay;
		}
		final SkypeUser skypeUser = userList.findByDisplayName(userDisplay);
		
		final String userId = skypeUser.getUserId();

		Date time = makeTimeFrom(timeAndUserInfo[0]);

		final SkypeChatMessage skypeChatMessage = skypeChatMessageDataFactory
				.produce(userId, userDisplay, message, time);
		return skypeChatMessage;
	}

	private Date makeTimeFrom(String messageTimeText) {
		try {
			return skypeMessageDateFormat.parse(messageTimeText);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	private Date makeChatTime(String chatTime) {
		try {
			return skypeChatDateFormat.parse(chatTime);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	private UsersSortedByUserId makeUserList(String userLines) {
		final String[] posters = userLines.split("\n");
		final Pattern pattern = Pattern
				.compile("Poster: id=([a-z0-9]*); display=(.*)");

		UsersSortedByUserId skypeUserList = new UsersSortedByUserId();
		for (String posterLine : posters) {
			Matcher matcher = pattern.matcher(posterLine);
			if (!matcher.find()) {
				throw new IllegalStateException(
						"Invalid poster pattern found: " + posterLine);
			}
			skypeUserList.add(new SkypeUserImpl(matcher.group(1), matcher
					.group(2)));
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
