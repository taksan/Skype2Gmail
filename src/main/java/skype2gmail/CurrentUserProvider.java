package skype2gmail;

import skype.SkypeUser;

import com.google.inject.ImplementedBy;

@ImplementedBy(CurrentUserProviderImpl.class)
public interface CurrentUserProvider {

	SkypeUser getUser();

}
