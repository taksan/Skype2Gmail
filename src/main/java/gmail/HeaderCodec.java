package gmail;

import java.io.UnsupportedEncodingException;

import javax.mail.internet.MimeUtility;

public class HeaderCodec {

	public String decodeText(String aHeader) {
		try {
			String decodedText = MimeUtility.decodeText(aHeader);
			String decodedWithoutMailLineBreaks = decodedText.replaceAll("(?<!\\\\)[\r\n]", "");
			return unescape(decodedWithoutMailLineBreaks);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String encodeText(String value) {
		try {
			return escape(MimeUtility.encodeText(value));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	private String escape(String encodeText) {
		return encodeText.replaceAll("([\\\\\r\n])","\\\\$1");
	}


	private String unescape(String decodedWithoutMailLineBreaks) {
		return decodedWithoutMailLineBreaks.replaceAll("\\\\(\\\\|\r|\n)", "$1");
	}
}
