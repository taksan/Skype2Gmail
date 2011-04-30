package skype2gmail;

import gmail.GmailMessage;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.mail.search.SearchTerm;

import org.apache.log4j.Logger;

import skype.SkypeChat;
import utils.LoggerProvider;

import com.google.inject.Inject;

public class FolderIndexImpl implements FolderIndex {
	
	Map<String,String> idToSignature;
	private final GmailFolder folder;
	private final GmailMessageFactory gmailMessageFactory;
	private Logger logger;

	@Inject
	public FolderIndexImpl(GmailFolder folder, GmailMessageFactory gmailMessageFactory, LoggerProvider loggerProvider) {
		this.folder = folder;
		this.gmailMessageFactory = gmailMessageFactory;
		this.logger = loggerProvider.getLogger(getClass());
	}

	@Override
	public String getSignatureFor(String id) {
		if (idToSignature == null) {
			retrieveIndexFromMail();
		}
		return idToSignature.get(id);
	}

	private void retrieveIndexFromMail() {
		this.logger.info("Retrieving skype chat index.");
		idToSignature = new LinkedHashMap<String, String>();
		
		final SearchTerm st = FolderIndex.CHAT_INDEX_SEARCH_TERM;
		GmailMessage indexMailMessage = folder.retrieveFirstMessageMatchingSearchTerm(st);
		
		String indexMessage = indexMailMessage.getBody();
		if (indexMessage == null) {
			this.logger.info("No index found. Will do a full scan on all messages");
			return;
		}
		this.logger.info("Index found.");
		String[] lines = indexMessage.split("\n");
		for (String aLine : lines) {
			String[] columns = aLine.split(",");
			String id = columns[0];
			String bodySignature = columns[1].trim();
			idToSignature.put(id, bodySignature);
		}
	}

	@Override
	public void addIndexFor(SkypeChat skypeChat) {
		idToSignature.put(skypeChat.getId(), skypeChat.getBodySignature());
	}

	@Override
	public void save() {
		this.logger.info("Saving chat index.");
		String indexBody = convertMapToString();
		GmailMessage indexMailMessage = gmailMessageFactory.factory();
		indexMailMessage.setCustomHeader(FolderIndex.INDEX_HEADER_NAME, FolderIndex.INDEX_HEADER_VALUE);
		indexMailMessage.setFrom("Skype2Gmail");
		indexMailMessage.setBody(indexBody);
		indexMailMessage.setSubject("Skype2Gmail chat index");
		folder.replaceMessageMatchingTerm(FolderIndex.CHAT_INDEX_SEARCH_TERM, indexMailMessage);
		this.logger.info("Chat index saved successfully.");
	}

	private String convertMapToString() {
		StringBuilder sb = new StringBuilder();
		Set<String> chatIdSet = idToSignature.keySet();
		for (String chatId : chatIdSet) {
			sb.append(chatId);
			sb.append(",");
			String bodySignature = idToSignature.get(chatId);
			sb.append(bodySignature);
			sb.append("\n");
		}
		return sb.toString().trim();
	}
}
