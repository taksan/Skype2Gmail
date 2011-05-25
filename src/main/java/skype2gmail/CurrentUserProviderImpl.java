package skype2gmail;

import skype.SkypeApi;
import skype.SkypeUser;

import com.google.inject.Inject;

public class CurrentUserProviderImpl implements CurrentUserProvider {
	
	private final SkypeApi skypeApi;

	@Inject
	public CurrentUserProviderImpl(SkypeApi skypeApi)
	{
		this.skypeApi = skypeApi;
		
	}

	@Override
	public SkypeUser getUser() {
		return this.skypeApi.getCurrentUser();
	}

}
