package skype.commons;

public interface SkypeUserFactory {

	SkypeUser produce(String userId, String displayName);

}
