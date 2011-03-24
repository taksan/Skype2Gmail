package skype2disk;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import skype.SkypeChat;
import skype.SkypeChatDateFormat;
import skype.SkypeChatFactory;
import skype.SkypeChatWithBodyParserFactory;

import com.google.inject.Inject;

public class FileDumpContentParserImpl implements FileDumpContentParser {

	
	private final SkypeChatDateFormat skypeChatDateFormat;
	private final SkypeChatWithBodyParserFactory skypeChatWithBodyParserFactory;
	private final SkypeChatFactory skypeChatFactory;

	@Inject
	public FileDumpContentParserImpl(
			SkypeChatDateFormat skypeChatDateFormat,
			SkypeChatWithBodyParserFactory skypeChatWithBodyParserFactory,
			SkypeChatFactory skypeChatFactory) 
	{
		this.skypeChatDateFormat = skypeChatDateFormat;
		this.skypeChatWithBodyParserFactory = skypeChatWithBodyParserFactory;
		this.skypeChatFactory = skypeChatFactory;
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
		if (messageSections.length < 2) {
			return skypeChatFactory.produceEmpty();
		}

		final String[] headersSections = messageSections[0].split("(?=Poster:.*)", 2);
		final String headerSection = headersSections[0];
		final String postersConcatenated = headersSections[1];
		final String bodySection = messageSections[1];

		final Map<String, String> parsedContents = extractHeaders(headerSection);
		final String[] posters = postersConcatenated.split("\n");

		final String[] messageSignatures = parsedContents
				.get("Messages signatures").replaceAll("[\\[\\]]", "")
				.split(",");

		final Date chatTime = makeChatTime(parsedContents.get("Chat Time"));
		final String chatId = parsedContents.get("Chat Id");
		final String topic = parsedContents.get("Chat topic");
		final String parsedSignature = parsedContents.get("Chat Body Signature");
		
		return skypeChatWithBodyParserFactory.produce(
				chatId, 
				topic,
				posters, 
				parsedSignature, 
				messageSignatures, 
				chatTime, 
				bodySection);
	}

	

	private Date makeChatTime(String chatTime) {
		return skypeChatDateFormat.parse(chatTime);
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
