package skype;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.NotImplementedException;

import utils.LoggerProvider;

public class EmptySkypeChat implements SkypeChat {

	private final LoggerProvider loggerProvider;
	
	public EmptySkypeChat(LoggerProvider loggerProvider) {
		this.loggerProvider = loggerProvider;
	}

	@Override
	public String getBodySignature() {
		return "";
	}
	
	@Override
	public String getId() {
		return "";
	}
	
	@Override
	public Date getTime() {
		return Calendar.getInstance().getTime();
	}
	
	@Override
	public String getTopic() {
		return "";
	}
	
	
	@Override
	public Date getLastModificationTime() {
		throw new NotImplementedException();
	}
	
	@Override
	public TimeSortedMessages getChatMessages() {
		throw new NotImplementedException();
	}

	@Override
	public UsersSortedByUserId getPosters() {
		throw new NotImplementedException();
	}

	@Override
	public SkypeChat merge(SkypeChat skypeChat) {
		loggerProvider.getLogger(SkypeChat.class).info("New chat created");
		return skypeChat;
	}

	@Override
	public SkypeUser getChatAuthor() {
		throw new NotImplementedException();
	}
}
