package skype.commons;

import com.google.inject.ImplementedBy;

@ImplementedBy(SkypeRecorder.class)
public interface SkypeHistoryRecorder {
	void record();
}
