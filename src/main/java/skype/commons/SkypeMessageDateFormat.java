package skype.commons;

import java.util.Date;

import com.google.inject.ImplementedBy;

@ImplementedBy(SkypeMessageDateFormatImpl.class)
public interface SkypeMessageDateFormat {

	String format(Date time);

	Date parse(String messageTimeText);

}
