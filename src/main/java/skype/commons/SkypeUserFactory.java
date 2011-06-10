package skype.commons;

import com.google.inject.ImplementedBy;

@ImplementedBy(SkypeUserFactoryImpl.class)
public interface SkypeUserFactory {

	SkypeUser produce(String userId, String displayName);

}
