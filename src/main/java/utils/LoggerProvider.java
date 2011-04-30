package utils;

import org.apache.log4j.Logger;

public interface LoggerProvider {

	Logger getLogger(Class<?> loggerClass);
	Logger getPriorityLogger(Class<?> forClass);
	Logger getLogger(String loggerName);
}
