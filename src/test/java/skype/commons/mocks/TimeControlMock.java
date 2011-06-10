package skype.commons.mocks;

import java.util.concurrent.CountDownLatch;

import skype2gmail.commons.TimerControl;

public class TimeControlMock implements TimerControl {
	CountDownLatch stepInvokeLatch = new CountDownLatch(1);
	CountDownLatch sleepInvokeLatch = new CountDownLatch(1);

	@Override
	public void sleep(int secondsToSleep) throws InterruptedException {
		sleepInvokeLatch.countDown();
		stepInvokeLatch.await();
		stepInvokeLatch = new CountDownLatch(1);
	}
	
	public void step() throws InterruptedException {
		sleepInvokeLatch.await();
		sleepInvokeLatch = new CountDownLatch(1);
		
		stepInvokeLatch.countDown();
	}
}
