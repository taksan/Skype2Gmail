package skype2gmail.mocks;

import skype.commons.SkypeHistoryRecorder;

public class SKypeHistoryRecorderMock implements SkypeHistoryRecorder {

	public boolean recordWasInvoked = false;
	private final String mockName;

	public SKypeHistoryRecorderMock(String mockName) {
		this.mockName = mockName;
	}

	@Override
	public void record() {
		recordWasInvoked = true;
	}

	public String getMockName() {
		return mockName;
	}

}
