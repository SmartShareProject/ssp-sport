package com.ssp.api.util;

import java.io.UnsupportedEncodingException;
import java.util.BitSet;

import org.apache.commons.lang.StringUtils;

public class AssistorConvertUtil {
	 /**
     * 将BitSet对象转化为ByteArray
     * @param bitSet BitSet
     * @return bytes
     */
    public static byte[] bitSet2ByteArray(BitSet bitSet) {
        byte[] bytes = new byte[bitSet.size() / 8];
        for (int i = 0; i < bitSet.size(); i++) {
            int index = i / 8;
            int offset = 7 - i % 8;
            bytes[index] |= (bitSet.get(i) ? 1 : 0) << offset;
        }
        return bytes;
    }

	// 字节字符串转换为字节数组
	public static byte[] BytesStrToBytes(String bytesStr) {
		char[] chars = bytesStr.toCharArray();
		byte[] bytes = new byte[bytesStr.length() / 2];
		for (int i = 0, j = 0, l = bytesStr.length(); i < l; i++, j++) {
			String swap = "" + chars[i++] + chars[i];
			int byteInt = Integer.parseInt(swap, 16) & 0xFF;
			bytes[j] = new Integer(byteInt).byteValue();
		}
		return bytes;
	}

	// 字节数组转换为字节字符串
	public static String BytesToBytesStr(byte[] bytes) {
		StringBuilder sBuffer = new StringBuilder();
		for (byte aByte : bytes) {
			sBuffer.append(Integer.toString((aByte & 0xFF) + 0x100, 16).substring(1));
		}
		return sBuffer.toString().toUpperCase();
	}


	// 字节字符串转换为比特字符串
	public static String BytesStrToBitsStr(String bytesStr) {
		return BytesToBitsStr(BytesStrToBytes(bytesStr));
	}

	// 比特字符串转换为字节字符串
	public static String BitsStrToBytesStr(String bitsStr) {
		return BytesToBytesStr(BitsStrToBytes(bitsStr)).toUpperCase();
	}

	// 比特字符串转换为字节数组
	public static byte[] BitsStrToBytes(String bitsStr) {
		bitsStr = bitsStr.replace(" ", "");
		byte[] bytes = new byte[bitsStr.length() / 8];
		for (int i = 0; i < bytes.length; i++) {
			int byteInt = Integer.parseInt(bitsStr.substring(i * 8, (i + 1) * 8), 2) & 0xFF;
			bytes[i] = new Integer(byteInt).byteValue();
		}
		return bytes;
	}

	// 字节数组转换为比特字符串
	public static String BytesToBitsStr(byte[] bytes) {
		StringBuilder sBuffer = new StringBuilder();
		int pos = 0;
		for (byte b : bytes) {
			// 高四位
			pos = (b & 0xF0) >> 4;
			sBuffer.append(StringUtils.leftPad(DecToBit(pos), 4, '0'));
			// 低四位
			pos = b & 0x0F;
			sBuffer.append(StringUtils.leftPad(DecToBit(pos), 4, '0'));
		}
		return sBuffer.toString();
	}

	// 字节字符串转换为字符串
	public static String BytesStrToStr(String bytesStr) {
		try {
			return new String(BytesStrToBytes(bytesStr), "GBK");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	// 字节字符串转换为字符串
	public static String BytesStrToStr(String bytesStr, String encoding) {
		try {
			return new String(BytesStrToBytes(bytesStr), encoding);
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	// 字符串转换为字节字符串
	public static String StrToBytesStr(String str) {
		try {
			return BytesToBytesStr(str.getBytes("GBK")).toUpperCase();
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	// 字符串转换为字节字符串
	public static String StrToBytesStr(String str, String encoding) {
		try {
			return BytesToBytesStr(str.getBytes(encoding)).toUpperCase();
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	// 10进制转换为2进制
	public static String DecToBit(int dec) {
		StringBuilder BitStr = new StringBuilder(Integer.toBinaryString(dec));
		while (BitStr.length() % 4 > 0) {
			BitStr.insert(0, "0");
		}
		return BitStr.toString();
	}

	// 10进制转换为16进制
	public static String DecToHex(int dec) {
		return Integer.toHexString(dec).toUpperCase();
	}

	// 16进制转换为2进制
	public static String HexToBit(String hex) {
		return DecToBit(HexToDec(hex));
	}

	// 16进制转换为10进制
	public static int HexToDec(String hex) {
		return Integer.valueOf(hex, 16);
	}
}
