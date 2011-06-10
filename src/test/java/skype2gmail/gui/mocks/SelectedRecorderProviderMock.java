package skype2gmail.gui.mocks;

import skype.commons.SkypeHistoryRecorder;
import skype.commons.SelectedRecorderProvider;
import skype2gmail.mocks.SKypeHistoryRecorderMock;

public class SelectedRecorderProviderMock implements SelectedRecorderProvider {
	final SkypeHistoryRecorder mock = new SKypeHistoryRecorderMock("mock");

	@Override
	public SkypeHistoryRecorder getSelected() {
		return mock;
	}

}
