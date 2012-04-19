package skype2gmail;


import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.mail.search.SearchTerm;

import mail.SkypeMailFolder;
import mail.SkypeMailMessage;
import mail.skypemail.SkypeMailMessageFactory;

import org.apache.log4j.Logger;

import skype.commons.SkypeChat;
import skype.commons.SkypeChatDateFormatImpl;
import utils.LoggerProvider;

import com.google.inject.Inject;

public class FolderIndexImpl implements FolderIndex {
	
	Map<String,String> idToSignature;
	private final SkypeMailFolder folder;
	private final SkypeMailMessageFactory gmailMessageFactory;
	private Logger logger;
	private final CurrentUserProvider currentUser;
	private final CurrentTimeProvider currentTimeProvider;

	@Inject
	public FolderIndexImpl(SkypeMailFolder folder, 
			SkypeMailMessageFactory gmailMessageFactory, 
			LoggerProvider loggerProvider, 
			CurrentUserProvider currentUser,
			CurrentTimeProvider currentTimeProvider) {
		this.folder = folder;
		this.gmailMessageFactory = gmailMessageFactory;
		this.currentUser = currentUser;
		this.currentTimeProvider = currentTimeProvider;
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
		SkypeMailMessage indexMailMessage = folder.retrieveSingleMessageMatchingSearchTerm(st);
		
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
		SkypeMailMessage indexMailMessage = gmailMessageFactory.factory();
		indexMailMessage.setCustomHeader(FolderIndex.INDEX_HEADER_NAME, FolderIndex.INDEX_HEADER_VALUE);
		indexMailMessage.setFrom("Skype2Gmail");
		indexMailMessage.addRecipient(currentUser.getUser());
		indexMailMessage.setBody(indexBody);
		Date sentTime = this.currentTimeProvider.now();
		SkypeChatDateFormatImpl df = new SkypeChatDateFormatImpl();
		indexMailMessage.setSubject("Skype2Gmail chat index - last update " + df.format(sentTime));
		indexMailMessage.setSentDate(sentTime);
		indexMailMessage.setDate(sentTime.toString());
		folder.replaceMessageMatchingTerm(FolderIndex.CHAT_INDEX_SEARCH_TERM, indexMailMessage);
		this.logger.info("Chat index saved successfully.");
	}

	private String convertMapToString() {
		if (idToSignature == null) {
			return "";
		}
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
