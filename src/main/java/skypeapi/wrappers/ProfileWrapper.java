package skypeapi.wrappers;

import com.skype.SkypeException;

public interface ProfileWrapper {

	String getId() throws SkypeException;

	String getFullName() throws SkypeException;

}
