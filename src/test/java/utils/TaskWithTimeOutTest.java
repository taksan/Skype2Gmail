package utils;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import junit.framework.Assert;

import org.junit.Test;

import skype.exceptions.ApplicationException;

public class TaskWithTimeOutTest {
	@Test
	public void happyDay() {
		Callable<Integer> aCallable = new Callable<Integer>() {
			
			@Override
			public Integer call() throws Exception {
				return 1;
			}
		};
		TaskWithTimeOut<Integer> taskWithTimeOut = new TaskWithTimeOut<Integer>(aCallable);
		Integer result = taskWithTimeOut.run();
		Assert.assertEquals(new Integer(1), result);
	}
	
	@Test
	public void timeout() throws Throwable {
		Callable<Integer> aCallable = new Callable<Integer>() {
			
			@Override
			public Integer call() throws Exception {
				Thread.sleep(100);
				return 1;
			}
		};
		TaskWithTimeOut<Integer> taskWithTimeOut = new TaskWithTimeOut<Integer>(aCallable);
		int timeout = 1;
		try {
			taskWithTimeOut.run(timeout, TimeUnit.MILLISECONDS);
		}catch (ApplicationException e) {
			try {
				throw e.getCause();
			} catch(TimeoutException e1) {
				return;
			}
		}
		Assert.fail("A TimeoutException should have been thrown!");
	}
}
