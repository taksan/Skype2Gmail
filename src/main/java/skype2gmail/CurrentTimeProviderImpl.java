package skype2gmail;

import java.util.Calendar;
import java.util.Date;

public class CurrentTimeProviderImpl implements CurrentTimeProvider {

	@Override
	public Date now() {
		return Calendar.getInstance().getTime();
	}

}
