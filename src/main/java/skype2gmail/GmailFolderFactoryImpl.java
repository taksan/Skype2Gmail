package skype2gmail;

import gmail.GmailFolder;

import javax.mail.Folder;

import utils.LoggerProvider;

import com.google.inject.Inject;

public class GmailFolderFactoryImpl implements GmailFolderFactory {

	private final LoggerProvider loggerProvider;
	@Inject
	public GmailFolderFactoryImpl(LoggerProvider loggerProvider) {
		this.loggerProvider = loggerProvider;
		
	}
	@Override
	public GmailFolder produce(Folder root) {
		return new GmailFolderImpl(root, loggerProvider);
	}

}
