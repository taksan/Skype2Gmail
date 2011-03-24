package utils;

import org.apache.log4j.Logger;

public class SimpleLoggerProvider implements LoggerProvider {

	@Override
	public Logger getLogger(Class<?> clazz) {
		return Logger.getLogger(clazz);
	}

}
