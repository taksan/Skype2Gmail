package utils;

import org.apache.log4j.Logger;

import com.google.inject.ImplementedBy;

@ImplementedBy(LoggerProviderImpl.class)
public interface LoggerProvider {

	Logger getLogger(Class<?> loggerClass);
	Logger getPriorityLogger(Class<?> forClass);
	Logger getLogger(String loggerName);
}
