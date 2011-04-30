package skype2gmail.mocks;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.URLName;

import org.apache.commons.lang.NotImplementedException;

import skype2gmail.SessionProviderImpl;

public class JavaMailStoreMock extends Store {

	protected JavaMailStoreMock() {
		super(new SessionProviderImpl().getInstance(),null);
	}

	@Override
	public Folder getDefaultFolder() throws MessagingException {
		throw new NotImplementedException();
	}

	@Override
	public Folder getFolder(String name) throws MessagingException {
		throw new NotImplementedException();
	}

	@Override
	public Folder getFolder(URLName url) throws MessagingException {
		throw new NotImplementedException();
	}

}
