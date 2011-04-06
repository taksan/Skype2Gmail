package skype;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import skype2disk.SkypeMessageParsingException;

public class SkypeUserNotFound implements InvocationHandler {

	public static SkypeUser produce(String userDisplay) {
		return (SkypeUser) Proxy.newProxyInstance(
				SkypeUser.class.getClassLoader(), 
				new Class<?>[]{ SkypeUser.class}, 
				new SkypeUserNotFound(userDisplay));
	}

	private final String userDisplay;

	public SkypeUserNotFound(String userDisplay) {
		this.userDisplay = userDisplay;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		throw new SkypeMessageParsingException(
				"User %s was found on chat, but was not among its posters!",
				userDisplay);
	}


}
