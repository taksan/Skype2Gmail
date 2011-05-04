package mail;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import skype.ApplicationException;
import skype2gmail.SessionProvider;
import skype2gmail.UserCredentialsProvider;

import com.google.inject.Inject;

public class SkypeGmailStore implements SkypeMailStore {
	private Store store;
	private final Session session;
	private final UserCredentialsProvider userInfoProvider;
	private Map<String, Folder> openedFoldersCached = new LinkedHashMap<String, Folder>();

	@Inject
	public SkypeGmailStore(SessionProvider sessionProvider,
			UserCredentialsProvider userInfoProvider) 
	{
		this.userInfoProvider = userInfoProvider;
		this.session = sessionProvider.getInstance();
	}

	@Override
	public Folder getFolder(String folderName) {
		if (isCached(folderName)) {
			return openedFoldersCached.get(folderName);
		}
		try {
			return retrieveFolder(folderName);
		} catch (NoSuchProviderException e) {
			throw new ApplicationException(e);
		} catch (MessagingException e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public void close() {
		try {
			store.close();
		} catch (MessagingException e) {
			throw new ApplicationException(e);
		}
	}
	

	private Folder retrieveFolder(String folderName)
			throws NoSuchProviderException, MessagingException {
		connectToStore();

		Folder retrievedFolder = store.getFolder(folderName);
		openFolderOrCreateIfItDoesnExist(retrievedFolder);
		cacheFolder(folderName, retrievedFolder);
		return retrievedFolder;
	}

	private void cacheFolder(String folderName, Folder retrievedFolder) {
		openedFoldersCached.put(folderName, retrievedFolder);
	}

	private void openFolderOrCreateIfItDoesnExist(Folder retrievedFolder)
			throws MessagingException {
		if (!retrievedFolder.exists()) {
			retrievedFolder.create(Folder.HOLDS_MESSAGES);
		} else {
			retrievedFolder.open(Folder.READ_WRITE);
		}
	}

	private void connectToStore() throws NoSuchProviderException,
			MessagingException {
		String user = userInfoProvider.getUser();
		String password = userInfoProvider.getPassword();
		store = session.getStore("imaps");
		store.connect("imap.gmail.com", user, password);
	}

	private boolean isCached(String folderName) {
		return openedFoldersCached.get(folderName) != null;
	}
}
