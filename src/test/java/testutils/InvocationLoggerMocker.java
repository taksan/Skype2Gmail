package testutils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InvocationLoggerMocker implements InvocationHandler {
	StringBuffer invocationLogger = new  StringBuffer();

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		invocationLogger.append(method.getName());
		return null;
	}
	
	public String getInvocations() {
		return invocationLogger.toString();
	}

	@SuppressWarnings("unchecked")
	public static <T> T create(
			Class<T> aClass) {
		return (T) Proxy.newProxyInstance(
				aClass.getClassLoader(), 
				new Class<?>[]{aClass}, 
				new InvocationLoggerMocker());
	}

}
