package com.noisyle.demo.crypto;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

 
public class CryptoUtils {

	final private static String CRYPTO_ENCODING = "UTF-8";

	public static String getMD5(String str, String encoding, int no_Lower_Upper) {
		if (null == encoding)
			encoding = CRYPTO_ENCODING;
		StringBuffer sb = new StringBuffer();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] array = md.digest(str.getBytes(encoding));
			for (int i = 0; i < array.length; i++) {
				sb.append(String.format("%02X", array[i]).toUpperCase());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (no_Lower_Upper == 0) {
			return sb.toString();
		}
		if (no_Lower_Upper == 1) {
			return sb.toString().toLowerCase();
		}
		if (no_Lower_Upper == 2) {
			return sb.toString().toUpperCase();
		}
		return null;
	}

	public static String getAES(String key, String content, int type) {
		try {
			byte[] keyBytes = key.getBytes(CRYPTO_ENCODING);
			MessageDigest md = MessageDigest.getInstance("MD5");
			keyBytes = md.digest(keyBytes);
			if (type == 0) { // 0为加密
				byte[] bytes = content.getBytes(CRYPTO_ENCODING);
				return byteArr2HexStr(encryptAES(bytes, keyBytes, false, keyBytes));
			} else {
				byte[] bytes = hexStr2ByteArr(content);
				return new String(decryptAES(bytes, keyBytes, false, keyBytes), CRYPTO_ENCODING);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * AES加密
	 *
	 * @param content
	 *            需要加密的内容
	 * @param key
	 *            加密密码
	 * @param md5Key
	 *            是否对key进行md5加密
	 * @param iv
	 *            加密向量
	 * @return 加密后的字节数据
	 */
	public static byte[] encryptAES(byte[] content, byte[] key, boolean md5Key, byte[] iv) {
		try {
			if (md5Key) {
				MessageDigest md = MessageDigest.getInstance("MD5");
				key = md.digest(key);
			}
			SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding"); // "算法/模式/补码方式"
			IvParameterSpec ivps = new IvParameterSpec(iv);// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivps);
			return cipher.doFinal(content);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static byte[] decryptAES(byte[] content, byte[] key, boolean md5Key, byte[] iv) {
		try {
			if (md5Key) {
				MessageDigest md = MessageDigest.getInstance("MD5");
				key = md.digest(key);
			}
			SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding"); // "算法/模式/补码方式"
			IvParameterSpec ivps = new IvParameterSpec(iv);// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivps);
			return cipher.doFinal(content);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private static byte[] hexStr2ByteArr(String strIn) throws Exception {
		byte[] arrB = strIn.getBytes();
		int iLen = arrB.length;

		// 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
		byte[] arrOut = new byte[iLen / 2];
		for (int i = 0; i < iLen; i = i + 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
		}
		return arrOut;
	}

	private static String byteArr2HexStr(byte[] arrB) throws Exception {
		int iLen = arrB.length;
		// 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
		StringBuffer sb = new StringBuffer(iLen * 2);
		for (int i = 0; i < iLen; i++) {
			int intTmp = arrB[i];
			// 把负数转换为正数
			while (intTmp < 0) {
				intTmp = intTmp + 256;
			}
			// 小于0F的数需要在前面补0
			if (intTmp < 16) {
				sb.append("0");
			}
			sb.append(Integer.toString(intTmp, 16));
		}
		return sb.toString();
	}
}

/*
01 算法/模式/填充                16字节加密后数据长度        不满16字节加密后长度
02 AES/CBC/NoPadding             16                          不支持
03 AES/CBC/PKCS5Padding          32                          16
04 AES/CBC/ISO10126Padding       32                          16
05 AES/CFB/NoPadding             16                          原始数据长度
06 AES/CFB/PKCS5Padding          32                          16
07 AES/CFB/ISO10126Padding       32                          16
08 AES/ECB/NoPadding             16                          不支持
09 AES/ECB/PKCS5Padding          32                          16
10 AES/ECB/ISO10126Padding       32                          16
11 AES/OFB/NoPadding             16                          原始数据长度
12 AES/OFB/PKCS5Padding          32                          16
13 AES/OFB/ISO10126Padding       32                          16
14 AES/PCBC/NoPadding            16                          不支持
15 AES/PCBC/PKCS5Padding         32                          16
16 AES/PCBC/ISO10126Padding      32                          16


CryptoJS supports the following padding schemes:
 
    Pkcs7 (the default)
    Iso97971
    AnsiX923
    Iso10126
    ZeroPadding
    NoPadding 
*/
