package skype;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
			throw new RuntimeException(e);
		}
	}
}
