package utils;

import org.apache.log4j.Logger;

public interface LoggerProvider {

	Logger getLogger(Class<?> loggerClass);

}
