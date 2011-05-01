package skype2gmail;

import com.google.inject.ImplementedBy;

import skype.SkypeUser;

@ImplementedBy(CurrentUserProviderImpl.class)
public interface CurrentUserProvider {

	SkypeUser getUser();

}
