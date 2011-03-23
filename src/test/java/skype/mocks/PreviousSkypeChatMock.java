package skype.mocks;

import java.util.Date;

import org.apache.commons.lang.NotImplementedException;

import skype.SkypeChat;
import skype.SkypeUser;
import skype.TimeSortedMessages;
import skype.UsersSortedByUserId;

public class PreviousSkypeChatMock implements SkypeChat {

	@Override
	public TimeSortedMessages getChatMessages() {
		throw new NotImplementedException();
	}

	@Override
	public UsersSortedByUserId getPosters() {
		throw new NotImplementedException();
	}

	@Override
	public String getId() {
		return "id-mock";
	}

	@Override
	public Date getTime() {
		throw new NotImplementedException();
	}

	@Override
	public String getTopic() {
		throw new NotImplementedException();
	}

	@Override
	public String getBodySignature() {
		throw new NotImplementedException();
	}

	@Override
	public Date getLastModificationTime() {
		throw new NotImplementedException();
	}

	@Override
	public SkypeChat merge(SkypeChat skypeChat) {
		throw new NotImplementedException();
	}

	
	@Override
	public String toString() {
		return "<Previous Entry>";
	}

	@Override
	public SkypeUser getChatAuthor() {
		throw new NotImplementedException();
	}
}
