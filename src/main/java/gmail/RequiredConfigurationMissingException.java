package gmail;

@SuppressWarnings("serial")
public class RequiredConfigurationMissingException extends RuntimeException {

	public RequiredConfigurationMissingException(String msg) {
		super(msg);
	}

}
