package com.ssp.api.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.apache.log4j.Logger;

/*****
 * RSA绛惧悕宸ュ叿绫�
 * @author Yasong
 *
 */
public class RSAUtil {
	private static Logger log = Logger.getLogger(RSAUtil.class);
	private static RSAUtil instance;

	public static RSAUtil getInstance() {
		if (instance == null)
			return new RSAUtil();
		return instance;
	}

	/*****
	 * 
	 * 鍏挜銆佺閽ユ枃浠剁敓鎴�
	 * 
	 * @param keyPath锛氫繚瀛樻枃浠剁殑璺緞
	 * @param keyFlag锛氭枃浠跺悕鍓嶇紑
	 */
	private void generateKeyPair(String key_path, String name_prefix) {
		KeyPairGenerator keygen = null;
		try {
			keygen = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e1) {
			log.error(e1.getMessage());
		}
		SecureRandom secrand = new SecureRandom();
		secrand.setSeed("3500".getBytes());
		keygen.initialize(1024, secrand);
		KeyPair keys = keygen.genKeyPair();
		PublicKey pubkey = keys.getPublic();
		PrivateKey prikey = keys.getPrivate();

		String pubKeyStr = new String(org.apache.commons.codec.binary.Base64.encodeBase64(pubkey.getEncoded()));
		String priKeyStr = new String(org.apache.commons.codec.binary.Base64
				.encodeBase64(org.apache.commons.codec.binary.Base64.encodeBase64(prikey.getEncoded())));
		File file = new File(key_path);
		if (!file.exists()) {
			file.mkdirs();
		}

		try {
			FileOutputStream fos = new FileOutputStream(new File(key_path + name_prefix + "_RSAKey_private.txt"));
			fos.write(priKeyStr.getBytes());
			fos.close();

			fos = new FileOutputStream(new File(key_path + name_prefix + "_RSAKey_public.txt"));
			fos.write(pubKeyStr.getBytes());
			fos.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	/*****
	 * 
	 * 璇诲彇瀵嗛挜鏂囦欢鍐呭
	 * 
	 * @param key_file:鏂囦欢璺緞
	 * @return
	 */
	private static String getKeyContent(String key_file) {
		File file = new File(key_file);
		BufferedReader br = null;
		InputStream ins = null;
		StringBuffer sReturnBuf = new StringBuffer();
		try {
			ins = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(ins, "UTF-8"));
			String readStr = null;
			readStr = br.readLine();
			while (readStr != null) {
				sReturnBuf.append(readStr);
				readStr = br.readLine();
			}
		} catch (IOException e) {
			return null;
		} finally {
			if (br != null) {
				try {
					br.close();
					br = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (ins != null) {
				try {
					ins.close();
					ins = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sReturnBuf.toString();
	}

	/*****
	 * 
	 * RSA绛惧悕澶勭悊锛氱敓鎴愮鍚嶇粨鏋�
	 * 
	 * @param prikeyvalue锛歊SA绉侀挜
	 * @param sign_str锛氬緟绛惧悕瀛楃涓叉簮鍐呭
	 * @return
	 */
	public static String sign(String prikeyvalue, String sign_str) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64Util.byteToBase64Decoding(prikeyvalue));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey myprikey = keyf.generatePrivate(priPKCS8);

			Signature signet = Signature.getInstance("MD5withRSA");
			signet.initSign(myprikey);
			signet.update(sign_str.getBytes("UTF-8"));
			byte[] signed = signet.sign();

			return new String(org.apache.commons.codec.binary.Base64.encodeBase64(signed));
		} catch (Exception e) {
			log.error("绛惧悕澶辫触," + e.getMessage());
		}
		return null;
	}

	/*****
	 * 
	 * RSA绛惧悕鏍￠獙楠岃瘉
	 * 
	 * @param pubkeyvalue锛歊SA鍏挜
	 * @param sign_str锛氬緟绛惧悕瀛楃涓叉簮鍐呭
	 * @param signed_str锛歊SA绛惧悕缁撴灉
	 * @return
	 */
	public static boolean checksign(String pubkeyvalue, String sign_str, String signed_str) {
		try {
			X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(Base64Util.byteToBase64Decoding(pubkeyvalue));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey pubKey = keyFactory.generatePublic(bobPubKeySpec);
			byte[] signed = Base64Util.byteToBase64Decoding(signed_str);
			Signature signetcheck = Signature.getInstance("MD5withRSA");
			signetcheck.initVerify(pubKey);
			signetcheck.update(sign_str.getBytes("UTF-8"));
			return signetcheck.verify(signed);
		} catch (Exception e) {
			log.error("绛惧悕楠岃瘉寮傚父," + e.getMessage());
		}
		return false;
	}

	public static void main(String[] args) {
		getInstance();

		String RSA_YT_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMhKNA1Ws0H6PrZ8t1lQxhQjERj0hYf8QWBlF2DtlMajYU52WsiGIvid6iQQhJGc+aPNTf3MfWCWSHk2XRIYRpjoVPQ8Oz8sLF8j3pT3I2h2gDRNvO2xqX+x+jyFDMnAXm4uMyBYS9wabuhUchF5JkHT1A3rZZFYapPqMTj/zeEFAgMBAAECgYB+uPwwCFAIiYVOPqBe4U1CBmHV8TffLwpKLAvbptX/y/VQCHAt+Th9JqSyxsSpwLDuI4KZ9tzI1KzsDCpcvYFEMuoPNgwjZBFBsmTdXD+nxUTKVbTII6kITyzMMWDBnF8LxAicMKpYcRKaVOULCg/AHPGV32Efd4pH8cyJGcJ6TQJBAP+7+YygfcJLvxI9kk/2Se+dI//mX6WVh1V0RFgSl0cWry+xq9xTQofy0wU++TiXkA05aCJbwY0EjyodUOcpHkMCQQDIf3r3WVpW4Fx6t6B2geew4mllckFEHHDf0pXE5GWymccQHHxo6knFrzZ8F/97XwAIGTabNBXQiWd9G1DfEyMXAkEAow/84wpCpe0efEb+UDY+lqagGb+PJUne7UIhgfb4tr9kHQkxCF+egIj4vNOWndsmYwhDugS/uWc60iO3Pm4deQJAC3qA57hN27tsj/oDTcWSJiZQMmagJe4a6DV+LY+F4vu60clPthHzt0WYsPIOxllh/xSyc6A/v3ieXCM8Ngk6cQJBAJiX6nzlyLyHrHQ0jIdQ97bYtJTqh0ZC6bZ3PShCj3we/Cu+5v6L5Rmwx0s+OJ84OnWIopuuc5QwmOT53VRIntE=";
		String RSA_YT_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDISjQNVrNB+j62fLdZUMYUIxEY9IWH/EFgZRdg7ZTGo2FOdlrIhiL4neokEISRnPmjzU39zH1glkh5Nl0SGEaY6FT0PDs/LCxfI96U9yNodoA0Tbztsal/sfo8hQzJwF5uLjMgWEvcGm7oVHIReSZB09QN62WRWGqT6jE4/83hBQIDAQAB";

		String sign = sign(RSA_YT_PRIVATE, "123456789");
		boolean result1 = checksign(RSA_YT_PUBLIC, "123456789", sign);
		boolean result2 = checksign(RSA_YT_PUBLIC, "1234", sign);

		System.out.println("result:" + result1 + ", result2:" + result2);

	}
}
