package skypeapi.wrappers;

import com.skype.Profile;
import com.skype.SkypeException;

public class ProfileWrapperImpl implements ProfileWrapper {

	private final Profile profile;
	
	public static ProfileWrapper wrap(Profile profile2) {
		return new ProfileWrapperImpl(profile2);
	}

	@Override
	public String getId() throws SkypeException {
		return this.profile.getId();
	}

	@Override
	public String getFullName() throws SkypeException {
		return this.profile.getFullName();
	}

	private ProfileWrapperImpl(Profile profile) {
		this.profile = profile;
		
	}
}
