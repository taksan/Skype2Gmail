package skype2disk.mocks;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import skype.BasePath;
import testutils.IOHelper;

public class BasePathMock implements BasePath {

	private static final File CREATE_TEMP_DIR_OR_CRY = IOHelper.createTempDirOrCry();

	@Override
	public String getPath() {
		return CREATE_TEMP_DIR_OR_CRY.getAbsolutePath();
	}
	
	public void delete() {
		try {
			FileUtils.deleteDirectory(CREATE_TEMP_DIR_OR_CRY);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
