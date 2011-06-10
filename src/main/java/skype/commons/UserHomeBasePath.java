package skype.commons;

import com.google.inject.Singleton;

@Singleton
public class UserHomeBasePath implements BasePath {

	@Override
	public String getPath() {
		return System.getProperty("user.home");
	}

}
