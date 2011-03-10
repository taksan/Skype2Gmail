package utils;

import org.apache.commons.codec.digest.DigestUtils;

public class DigestProvider {
	public static final DigestProvider instance;
	static {
		instance = new DigestProvider();
	}
	
	private DigestProvider() {
	}
	
	public String encode(String data) {
		return DigestUtils.md5Hex(data);
	}
	
	public String extendedEncode(String data) {
		return DigestUtils.sha256Hex(data);
	}
}
