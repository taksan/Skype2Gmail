package utils;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.junit.Assert;
import org.junit.Test;

import com.skype.connector.UnsupportedArchitectureException;

public class ExceptionHandlingHelperTest {
	@Test
	public void testGetRootCause() {
		Throwable t1 = new Throwable();
		Throwable t2 = new Throwable(t1);
		Throwable t3 = new Throwable(t2);
		Throwable rootCause = ExceptionHandlingHelper.getRootCause(t3);
		
		Assert.assertEquals(t1, rootCause);
	}
	
	@Test
	public void testErrorMessageForUnsuppportedArchitecture() {
		UnsupportedArchitectureException actualException = new UnsupportedArchitectureException("FOO");
		IllegalStateException ex = new IllegalStateException(actualException);
		String errorMessage = ExceptionHandlingHelper.getExceptionMessageIfPossibleOrTheStackTrace(ex);
		
		Assert.assertEquals("FOO", errorMessage);
	}
	
	@Test
	public void testErrorMessageForForOtherExceptions() {
		RuntimeException actualException = new RuntimeException("Unhandled exception");
		IllegalStateException ex = new IllegalStateException(actualException);
		String errorMessage = ExceptionHandlingHelper.getExceptionMessageIfPossibleOrTheStackTrace(ex);
		
		String expected =String.format("%s\n%s",
			actualException.getClass().getName(),
			ExceptionUtils.getFullStackTrace(ex));
		
		Assert.assertEquals(expected, errorMessage);
	}
}
