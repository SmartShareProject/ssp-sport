package com.ssp.api.util;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

/**
 * 32位MD5摘要算法
 * @author shmily
 * @date 2012-6-28 下午02:34:45
 */
public class Md5Util {
	private static Logger logger = Logger.getLogger(Md5Util.class);
	private static Md5Util instance;

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	private Md5Util(){

	}

	public static Md5Util getInstance(){
		if(null == instance)
			return new Md5Util();
		return instance;
	}

	/**
	 * 转换字节数组为16进制字串
	 * @param b 字节数组
	 * @return 16进制字串
	 */
	private String byteArrayToHexString(byte[] b) {
		StringBuilder resultSb = new StringBuilder();
		for (byte value : b) {
			resultSb.append(byteToHexString(value));
		}
		return resultSb.toString();
	}

	/**
	 * 转换字节数组为高位字符串
	 * @param b 字节数组
	 * @return
	 */
	private String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * MD5 摘要计算(byte[]).
	 * @param src byte[]
	 * @return String
	 */
	public String md5Digest(byte[] src) {
		MessageDigest alg;
		try {
			// MD5 is 32 bit message digest
			alg = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		return byteArrayToHexString(alg.digest(src));
	}

	/***
	 * MD5 摘要计算(String).
	 * @param src String
	 * @param charsetName String
	 * @return String
	 */
	public String md5Digest(String src, String charsetName){
		try{
		if(src != null && src.length() != 0)
			return md5Digest(src.getBytes(charsetName));
		else
			return null;
		}catch(Exception e){
			logger.error("md5Digest failed:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		try {
			System.out.println(Md5Util.getInstance().md5Digest("88888".getBytes("iso8859-1")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
