package skype;


public class UserHomeBasePath implements BasePath {

	@Override
	public String getPath() {
		return System.getProperty("user.home");
	}

}
