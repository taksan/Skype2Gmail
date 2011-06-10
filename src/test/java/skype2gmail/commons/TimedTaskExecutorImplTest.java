package skype2gmail.commons;

import java.util.TimerTask;

import org.junit.Assert;
import org.junit.Test;

import skype.commons.mocks.TimeControlMock;

public class TimedTaskExecutorImplTest {
	@Test
	public void setTaskAndStart_ShouldInvokeTaskRun() throws InterruptedException {
		
		TimeControlMock timeController = new TimeControlMock();
		TimedTaskExecutorImpl subject = new TimedTaskExecutorImpl(timeController);
		
		TimerTaskMock taskMock = new TimerTaskMock();
		subject.setTask(taskMock, 1).start();
		
		timeController.step();
		Assert.assertEquals(1, taskMock.invocations);
		
		timeController.step();
		Assert.assertEquals(2, taskMock.invocations);
	}
	
	private static class TimerTaskMock extends TimerTask {

		public int invocations = 0;

		@Override
		public void run() {
			invocations++;
		}
	}
}
