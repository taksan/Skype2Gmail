package skype2disk.mocks;

import java.io.File;

import org.apache.commons.lang.NotImplementedException;

import skype.BasePath;
import testutils.IOHelper;

public class BasePathMock implements BasePath {

	private File CREATE_TEMP_DIR_OR_CRY;

	@Override
	public String getPath() {
		if (CREATE_TEMP_DIR_OR_CRY == null)
			CREATE_TEMP_DIR_OR_CRY = IOHelper.createTempDirOrCry();
		return CREATE_TEMP_DIR_OR_CRY.getAbsolutePath();
	}
	
	public void delete() {
		throw new NotImplementedException();
	}
}
