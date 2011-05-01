package skype2gmail;

import java.util.Date;

import com.google.inject.ImplementedBy;

@ImplementedBy(CurrentTimeProviderImpl.class)
public interface CurrentTimeProvider {

	Date now();
	
}
