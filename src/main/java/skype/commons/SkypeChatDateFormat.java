package skype.commons;

import java.util.Date;

import com.google.inject.ImplementedBy;

@ImplementedBy(SkypeChatDateFormatImpl.class)
public interface SkypeChatDateFormat {

	String format(Date time);

	Date parse(String time);

}
