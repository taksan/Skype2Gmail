package skype;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.NotImplementedException;

public class EmptySkypeChat implements SkypeChat {

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
	public UsersSortedByUserId getMembersIds() {
		throw new NotImplementedException();
	}

	@Override
	public SkypeChat merge(SkypeChat skypeChat) {
		return skypeChat;
	}
}
