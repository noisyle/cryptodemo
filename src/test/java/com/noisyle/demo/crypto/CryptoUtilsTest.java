package com.noisyle.demo.crypto;

import org.junit.Test;

public class CryptoUtilsTest {

	@Test
	public void testCrypto() throws Exception {
		String s = "hello";
		String key = "111";
		System.out.println(CryptoUtils.getAES(key, s, 0));
		System.out.println(CryptoUtils.getAES(key, CryptoUtils.getAES(key, s, 0), 1));
	}
}
