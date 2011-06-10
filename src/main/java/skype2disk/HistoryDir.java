package skype2disk;

import java.io.File;

import skype.commons.SkypeHistoryCli;

import com.google.inject.ImplementedBy;

@ImplementedBy(SkypeHistoryCli.class)
public interface HistoryDir {
	public File getHistoryDir();
}
