package skype2gmail.mocks;

import java.util.LinkedHashMap;
import java.util.Map;

import skype.SkypeChat;
import skype2gmail.FolderIndex;

public class FolderIndexMock implements FolderIndex {
	
	Map<String,String> idToSignature = new LinkedHashMap<String, String>();
	private boolean wasSaved;

	@Override
	public String getSignatureFor(String id) {
		return idToSignature.get(id);
	}

	public void addSignatureForId(String id, String signature) {
		idToSignature.put(id, signature);
	}

	@Override
	public void addIndexFor(SkypeChat skypeChat) {
		addSignatureForId(skypeChat.getId(), skypeChat.getBodySignature());
	}
	
	@Override
	public void save() {
		wasSaved = true;
	}

	public boolean wasSaved() {
		return wasSaved;
	}
}
