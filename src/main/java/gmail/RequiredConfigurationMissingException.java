package gmail;

import skype.ApplicationException;

@SuppressWarnings("serial")
public class RequiredConfigurationMissingException extends ApplicationException {

	public RequiredConfigurationMissingException(String msg) {
		super(msg);
	}

}
