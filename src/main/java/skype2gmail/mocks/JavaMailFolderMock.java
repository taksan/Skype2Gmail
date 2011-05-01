package skype2gmail.mocks;

import java.util.ArrayList;
import java.util.Arrays;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.search.SearchTerm;

import org.apache.commons.lang.NotImplementedException;

public class JavaMailFolderMock extends Folder {
	ArrayList<Message> storedMessages = new ArrayList<Message>();

	protected JavaMailFolderMock(Store store) {
		super(store);
	}

	@Override
	public boolean isOpen() {
		return true;
	}

	@Override
	public int getMessageCount() throws MessagingException {
		return storedMessages.size();
	}

	@Override
	public Message getMessage(int msgnum) throws MessagingException {
		int theMessageIsNot0Based = msgnum-1;
		return storedMessages.get(theMessageIsNot0Based);
	}
	
	@Override
	public void appendMessages(Message[] msgs) throws MessagingException {
		storedMessages.addAll(Arrays.asList(msgs));
	}
	
	@Override
	public Message[] search(SearchTerm term) throws MessagingException
	{
		for (Message aMessage : storedMessages) {
			if (!aMessage.isSet(Flags.Flag.DELETED)) {
				return new Message[]{aMessage};
			}
		}
		return new Message[0];
	}

	

	@Override
	public String getName() {
		throw new NotImplementedException();
	}

	@Override
	public String getFullName() {
		throw new NotImplementedException();
	}

	@Override
	public Folder getParent() throws MessagingException {
		throw new NotImplementedException();
	}

	@Override
	public boolean exists() throws MessagingException {
		return true;
	}

	@Override
	public Folder[] list(String pattern) throws MessagingException {
		throw new NotImplementedException();
	}

	@Override
	public char getSeparator() throws MessagingException {
		throw new NotImplementedException();
	}

	@Override
	public int getType() throws MessagingException {
		throw new NotImplementedException();
	}

	@Override
	public boolean create(int type) throws MessagingException {
		throw new NotImplementedException();
	}

	@Override
	public boolean hasNewMessages() throws MessagingException {
		throw new NotImplementedException();
	}

	@Override
	public Folder getFolder(String name) throws MessagingException {
		throw new NotImplementedException();
	}

	@Override
	public boolean delete(boolean recurse) throws MessagingException {
		throw new NotImplementedException();
	}

	@Override
	public boolean renameTo(Folder f) throws MessagingException {
		throw new NotImplementedException();
	}

	@Override
	public void open(int mode) throws MessagingException {
	}

	@Override
	public void close(boolean expunge) throws MessagingException {
	}

	@Override
	public Flags getPermanentFlags() {
		throw new NotImplementedException();
	}

	@Override
	public Message[] expunge() throws MessagingException {
		return new Message[0];
	}

}
