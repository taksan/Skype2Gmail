package utils;


import org.apache.commons.lang.exception.ExceptionUtils;

import com.skype.connector.UnsupportedArchitectureException;

public class ExceptionHandlingHelper {

	public static String getExceptionMessageIfPossibleOrTheStackTrace(
			IllegalStateException ex) {
		Throwable cause = ExceptionHandlingHelper.getRootCause(ex);
		if (cause != null && cause instanceof UnsupportedArchitectureException) {
			return cause.getMessage();
		}
		
		String stackTrace = ExceptionUtils.getFullStackTrace(ex);
		return
			String.format("%s\n%s", 
					cause.getClass().getName(),
					stackTrace);
	}

	public static Throwable getRootCause(Throwable ex) {
		while  (ex.getCause() != null) {
			ex = ex.getCause();
		}
		return ex;
	}

}
