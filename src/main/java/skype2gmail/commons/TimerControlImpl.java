package skype2gmail.commons;

public class TimerControlImpl implements TimerControl {

	@Override
	public void sleep(int secondsToSleep) throws InterruptedException {
		Thread.sleep(secondsToSleep * 1000);
	}

}
