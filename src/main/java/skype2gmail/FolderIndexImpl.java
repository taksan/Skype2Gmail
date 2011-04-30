package skype2gmail;

import gmail.GmailMessage;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.mail.search.SearchTerm;

import skype.SkypeChat;

import com.google.inject.Inject;

public class FolderIndexImpl implements FolderIndex {
	
	Map<String,String> idToSignature;
	private final GmailFolder folder;
	private final GmailMessageFactory gmailMessageFactory;

	@Inject
	public FolderIndexImpl(GmailFolder folder, GmailMessageFactory gmailMessageFactory) {
		this.folder = folder;
		this.gmailMessageFactory = gmailMessageFactory;
	}

	@Override
	public String getSignatureFor(String id) {
		if (idToSignature == null) {
			retrieveIndexFromMail();
		}
		return idToSignature.get(id);
	}

	private void retrieveIndexFromMail() {
		idToSignature = new LinkedHashMap<String, String>();
		
		final SearchTerm st = FolderIndex.CHAT_INDEX_SEARCH_TERM;
		GmailMessage indexMailMessage = folder.retrieveFirstMessageMatchingSearchTerm(st);
		
		String indexMessage = indexMailMessage.getBody();
		if (indexMessage == null)
			return;
		
		String[] lines = indexMessage.split("\n");
		for (String aLine : lines) {
			String[] columns = aLine.split(",");
			String id = columns[0];
			String bodySignature = columns[1];
			idToSignature.put(id, bodySignature);
		}
	}

	@Override
	public void addIndexFor(SkypeChat skypeChat) {
		idToSignature.put(skypeChat.getId(), skypeChat.getBodySignature());
	}

	@Override
	public void save() {
		String indexBody = convertMapToString();
		GmailMessage indexMailMessage = gmailMessageFactory.factory();
		indexMailMessage.setCustomHeader(FolderIndex.INDEX_HEADER_NAME, FolderIndex.INDEX_HEADER_VALUE);
		indexMailMessage.setBody(indexBody);
		folder.replaceMessageMatchingTerm(FolderIndex.CHAT_INDEX_SEARCH_TERM, indexMailMessage);
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
