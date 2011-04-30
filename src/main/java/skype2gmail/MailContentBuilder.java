package skype2gmail;

import java.util.Date;

import org.apache.commons.lang.NotImplementedException;

import skype.ChatContentBuilder;
import skype.SkypeChat;

public class MailContentBuilder implements ChatContentBuilder {

	private StringBuilder content;

	public MailContentBuilder(SkypeChat chat) {
		this.content.append("From:");
		
	}

	@Override
	public String getContent() {
		throw new NotImplementedException();
	}

	@Override
	public Date getLastModificationTime() {
		throw new NotImplementedException();
	}

}
