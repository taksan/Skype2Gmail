package skype.commons;

import java.util.Date;

public interface SkypeMessageDateFormat {

	String format(Date time);

	Date parse(String messageTimeText);

}