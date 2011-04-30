package skype2gmail;

import utils.Maybe;

public interface Skype2GmailConfigContents {
	public Maybe<String> getProperty(String key);
	public void setProperty(String key, String change);
}
