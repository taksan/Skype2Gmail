package testutils;

import utils.DigestProvider;

public class DigestProviderForTestFactory {
	final static DigestProvider digestProvider = new DigestProvider();
	public static DigestProvider getInstance() {
		return digestProvider;
	}
}
