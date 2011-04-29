package skype2gmail;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;

import com.google.inject.Inject;

import skype.SkypeChat;

public class FolderIndexImpl implements FolderIndex {
	
	Map<String,String> idToSignature;
	private final GmailFolder folder;

	@Inject
	public FolderIndexImpl(GmailFolder folder) {
		this.folder = folder;
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
		
		String indexMessage = folder.retrieveIndexFromMail();
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
		throw new NotImplementedException();
	}
}
