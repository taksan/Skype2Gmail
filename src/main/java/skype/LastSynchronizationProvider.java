package skype;

import java.util.Date;

public interface LastSynchronizationProvider {
	public static final String LAST_SYNCH_PLACE_HOLDER = ".lastSynchPlaceHolder";

	Date getLastSynch();

	void updateSynch();
}
