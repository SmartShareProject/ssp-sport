package com.ssp.api.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;



public class AssistorCrypt {
	// 算法名称
	public static final String KEY_ALGORITHM = "TripleDES";
	// 算法名称/加密模式/填充方式
	public static final String CIPHER_ALGORITHM_ECB = "TripleDES/ECB/NoPadding";
	public static final String CIPHER_ALGORITHM_CBC = "TripleDES/CBC/NoPadding";

	public static String TripleDES_Encrypt(String KeyStr, String DataStr) {
		String returnStr = new String();
		try {
				byte[] Key = AssistorConvertUtil.BytesStrToBytes(KeyStr);
				byte[] Data = AssistorConvertUtil.BytesStrToBytes(DataStr);
				Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB);
				SecretKey key = new SecretKeySpec(build3DesKey(Key), KEY_ALGORITHM); // 生成密钥
				cipher.init(Cipher.ENCRYPT_MODE, key);
				returnStr = AssistorConvertUtil.BytesToBytesStr(cipher.doFinal(Data));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnStr;
	}

	public static String TripleDES_Decrypt(String KeyStr, String DataStr) {
		String returnStr = new String();
		try {
			byte[] Key = AssistorConvertUtil.BytesStrToBytes(KeyStr);
			byte[] Data = AssistorConvertUtil.BytesStrToBytes(DataStr);
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB);
			SecretKey key = new SecretKeySpec(build3DesKey(Key), KEY_ALGORITHM); // 生成密钥
			cipher.init(Cipher.DECRYPT_MODE, key);
			returnStr = AssistorConvertUtil.BytesToBytesStr(cipher.doFinal(Data));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnStr;
	}

	private static byte[] build3DesKey(byte[] temp) {
		byte[] key = new byte[24]; // 声明一个24位的字节数组，默认里面都是0
		System.arraycopy(temp, 0, key, 0, temp.length);
		// 补充的8字节就是16字节密钥的前8位
		for (int i = 0; i < 8; i++) {
			key[16 + i] = temp[i];
		}
		return key;
	}
	public static String mac(  String key,String plain) {
		String mac = "";
		String src=AssistorConvertUtil.StrToBytesStr(plain);
		String mab = xor(src, 16);
		String mabHex = AssistorConvertUtil.StrToBytesStr(mab);
		String mabHex1 = mabHex.substring(0, 16);
		String mabHex2 = mabHex.substring(16, 32);
		
		String encryptStr = new String();
		encryptStr = AssistorCrypt.TripleDES_Decrypt( key, mabHex1);
		encryptStr += mabHex2;
		mab = xor(encryptStr, 16);
		encryptStr = AssistorCrypt.TripleDES_Decrypt(key, mab);
		mac = AssistorConvertUtil.StrToBytesStr(encryptStr.substring(0, 8));
				
		return mac;
	}
	private static String xor(String src, int xorLen) {
		while (src.length() % xorLen != 0) {
			src += "0";
		}
		int index = 0;
		String s1 = src.substring(index, index + xorLen);
		index += xorLen;
		while (true) {
			String s2 = src.substring(index, index + xorLen);
			index += xorLen;
			byte[] b1 = AssistorConvertUtil.BytesStrToBytes(s1);
			byte[] b2 = AssistorConvertUtil.BytesStrToBytes(s2);
			byte[] res = new byte[8];
			for (int i = 0; i < b1.length; i++) {
				res[i] = (byte) (b1[i] ^ b2[i]);
			}
			s1 = AssistorConvertUtil.BytesToBytesStr(res);
			if (index >= src.length()) {
				break;
			}
		}
		return s1.toUpperCase();
	}
}
