package skype.commons;

import com.google.inject.ImplementedBy;

@ImplementedBy(UserHomeBasePath.class)
public interface BasePath {

	String getPath();

}
