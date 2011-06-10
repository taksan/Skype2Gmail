package skype2gmail;

import mail.UserConfigBasedProvider;

import com.google.inject.ImplementedBy;

@ImplementedBy(UserConfigBasedProvider.class)
public interface UserCredentialsProvider {

	String getUser();

	String getPassword();

}
