package skype.mocks;

import java.util.Date;

import org.apache.commons.lang.NotImplementedException;

import skype.commons.LastSynchronizationProvider;

public class LastSynchronizationProviderMock implements LastSynchronizationProvider {

	private boolean updateWasInvoked = false;

	@Override
	public Date getLastSynch() {
		throw new NotImplementedException();
	}

	@Override
	public void updateSynch() {
		this.updateWasInvoked = true;
	}

	public boolean updateWasInvoked() {
		return updateWasInvoked;
	}

}
