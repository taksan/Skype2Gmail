package skype.commons;

import java.util.Date;

import com.google.inject.ImplementedBy;

@ImplementedBy(LastSynchronizationProviderImpl.class)
public interface LastSynchronizationProvider {
	public static final String LAST_SYNCH_PLACE_HOLDER = ".lastSynchPlaceHolder";

	Date getLastSynch();

	void updateSynch();
}
