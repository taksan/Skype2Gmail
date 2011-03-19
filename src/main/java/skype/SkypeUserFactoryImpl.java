package skype;

import com.google.inject.Inject;


public class SkypeUserFactoryImpl implements SkypeUserFactory {

	private SkypeApi skypeApi;
	
	@Inject
	public SkypeUserFactoryImpl(SkypeApi skypeApi) {
		this.skypeApi = skypeApi;
		
	}

	@Override
	public SkypeUser produce(String userId, String displayName) {
		boolean isCurrentUser = skypeApi.getCurrentUser().getUserId().equals(userId);
		return new SkypeUserImpl(userId, displayName, isCurrentUser);
	}
}
