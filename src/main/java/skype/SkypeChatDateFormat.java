package skype;

import java.util.Date;

public interface SkypeChatDateFormat {

	String format(Date time);

	Date parse(String time);

}
