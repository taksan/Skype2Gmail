package skype2gmail.gui.mocks;

import java.util.TimerTask;

import skype2gmail.gui.TimedTaskExecutor;
import skype2gmail.gui.TimedTaskExecutorStarter;

public class TimedTaskExecutorMock implements TimedTaskExecutor {
	TimerTask task;
	private boolean isRunning;
	
	public void tick() {
		if (!isRunning)
			throw new RuntimeException("Timer must have been started");
		this.task.run();
	}

	@Override
	public TimedTaskExecutorStarter setTask(TimerTask aTask, int secondsBetweenInvocations) {
		this.task = aTask;
		
		return new TimedTaskExecutorStarterMock();
	}

	private class TimedTaskExecutorStarterMock implements TimedTaskExecutorStarter {

		@Override
		public void start() {
			isRunning = true;
		}
	}
}
