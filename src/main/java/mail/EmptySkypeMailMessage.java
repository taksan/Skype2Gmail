package mail;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.commons.lang.NotImplementedException;

public class EmptySkypeMailMessage implements InvocationHandler {
	
	public static SkypeMailMessage create() {
		return (SkypeMailMessage) Proxy.newProxyInstance(
				SkypeMailMessage.class.getClassLoader(), 
				new Class<?>[]{SkypeMailMessage.class}, 
				new EmptySkypeMailMessage());
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		if (method.getName().equals("getBody"))
			return null;
		if (method.getName().equals("delete"))
			return null;
		throw new NotImplementedException();
	}
}
