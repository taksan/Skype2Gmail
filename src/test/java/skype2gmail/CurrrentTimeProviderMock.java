package skype2gmail;

import java.util.Date;

import testutils.DateHelper;

public class CurrrentTimeProviderMock implements CurrentTimeProvider {

	private Date mockedDate = DateHelper.makeDate(1979, 1, 23, 0, 13, 30);

	@Override
	public Date now() {
		return mockedDate;
	}

	public void setNow(int hour) {
		mockedDate = DateHelper.makeDate(1979, 1, 23, hour, 13, 30);
	}

}
