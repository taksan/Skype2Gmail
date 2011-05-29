package skype2gmail;

import skype.commons.SkypeUser;

import com.google.inject.ImplementedBy;

@ImplementedBy(CurrentUserProviderImpl.class)
public interface CurrentUserProvider {

	SkypeUser getUser();

}
