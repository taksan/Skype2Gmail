package utils;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.inject.Singleton;

@Singleton
public class DigestProvider {
	public String encode(String data) {
		return DigestUtils.md5Hex(data);
	}
	
	public String extendedEncode(String data) {
		return DigestUtils.sha256Hex(data);
	}
}
