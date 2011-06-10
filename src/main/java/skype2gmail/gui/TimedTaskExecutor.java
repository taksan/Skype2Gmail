package skype2gmail.gui;

import java.util.TimerTask;

public interface TimedTaskExecutor {

	TimedTaskExecutorStarter setTask(TimerTask createSynchronizeTask, int secondsBetweenInvocations);

}
