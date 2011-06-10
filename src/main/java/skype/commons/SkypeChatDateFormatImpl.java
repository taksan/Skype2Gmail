package skype.commons;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.inject.Singleton;

import skype.exceptions.MessageProcessingException;

@Singleton
public class SkypeChatDateFormatImpl implements SkypeChatDateFormat {
	private final SimpleDateFormat dateFormat;

	public SkypeChatDateFormatImpl() {
		dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	}

	@Override
	public String format(Date time) {
		return dateFormat.format(time);
	}

	@Override
	public Date parse(String time) {
		try {
			return dateFormat.parse(time);
		} catch (ParseException e) {
			throw new MessageProcessingException(e);
		}
	}
}
