package skype2gmail.mocks;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import org.apache.commons.lang.NotImplementedException;

public class JavaMailStoreMock extends Store {

	private static final Session MOCK_SESSION = Session.getDefaultInstance(new Properties(),null);

	public JavaMailStoreMock() {
		super(MOCK_SESSION,null);
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
