package skype2disk;

import java.util.Date;

import skype.EmptySkypeMessage;
import skype.SkypeChatMessage;
import skype.SkypeChatMessageDataFactory;
import skype.SkypeMessageDateFormat;
import skype.SkypeUser;
import skype.TimeSortedMessages;
import skype.UsersSortedByUserId;

public class MessageBodyParser {
	
	private final SkypeChatMessageDataFactory skypeChatMessageDataFactory;
	private final SkypeMessageDateFormat skypeMessageDateFormat;
	private final UsersSortedByUserId userList;
	private final String[] messageSignatures;

	public MessageBodyParser(
			SkypeChatMessageDataFactory skypeChatMessageDataFactory,
			SkypeMessageDateFormat skypeMessageDateFormat,
			UsersSortedByUserId userList,
			String[] messageSignatures) {
		this.skypeChatMessageDataFactory = skypeChatMessageDataFactory;
		this.skypeMessageDateFormat = skypeMessageDateFormat;
		this.userList = userList;
		this.messageSignatures = messageSignatures;
	}

	public TimeSortedMessages parse(String bodySection) {
		TimeSortedMessages messageList = new TimeSortedMessages();
		if (bodySection.trim().length() == 0)
			return messageList;
		
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
			if (!skypeChatMessage.isMatchingSignature(expectedSignature)) {
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
		if (line.trim().length() == 0) {
			return EmptySkypeMessage.produce();
		}
		
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
		final String userId = skypeUser.getUserId();
		Date time = makeTimeFrom(timeAndUserInfo[0]);
		final SkypeChatMessage skypeChatMessage = skypeChatMessageDataFactory
				.produce(userId, userDisplay, message, time);
		return skypeChatMessage;
	}
	
	private Date makeTimeFrom(String messageTimeText) {
		return skypeMessageDateFormat.parse(messageTimeText);
	}
}
