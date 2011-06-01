package skype.commons;

import com.skype.SkypeException;

public interface UserWrapper {

	String getId();

	String getFullName() throws SkypeException;

}
