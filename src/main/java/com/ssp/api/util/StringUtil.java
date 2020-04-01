package com.ssp.api.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;



public class StringUtil
{
	/**
	 * �ж�destStr�Ƿ�Ϊ�գ����Ϊ���򷵻�Ĭ��ֵ�����򷵻�destStr
	 * @param destStr
	 * @param defaultStr
	 * @return
	 */
	public static String validateParam(String destStr,String defaultStr){
		if(null == destStr || "".equals(destStr.trim())){
			return defaultStr;
		}else{
			return destStr;
		}
	}
	
	public static String intToString(Integer integer){
		if(integer == null){
			return "0";
		}else{
			return String.valueOf(integer);
		}
	}
	
	
	public static String Dollar2Cent(String s)
	{
		s = trim(s);
		int i = s.indexOf(".");
		if (i == -1)
			return s + "00";
		String intStr = s.substring(0, i);
		if (intStr.length() <= 0)
			intStr = "0";
		String decStr = s.substring(i + 1, s.length());
		if (decStr.length() <= 0)
			decStr = "00";
		if (decStr.length() == 1)
			decStr += "0";
		if (decStr.length() > 2)
			decStr = decStr.substring(0, 2);
		int iInt = Integer.parseInt(intStr);
		if (iInt <= 0)
			return decStr;
		return intStr + decStr;
	}

	public static String Cent2Dollar(String s)
	{
		long l = 0L;
		try
		{
			if (s.charAt(0) == '+')
				s = s.substring(1);
			l = Long.parseLong(s);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "";
		}
		boolean negative = false;
		if (l < 0L)
		{
			negative = true;
			l = Math.abs(l);
		}
		s = Long.toString(l);
		if (s.length() == 1)
			return negative ? "-0.0" + s : "0.0" + s;
		if (s.length() == 2)
			return negative ? "-0." + s : "0." + s;
		return (negative ? ("-" + s.substring(0, s.length() - 2) + "." + s.substring(s.length() - 2)) : (s.substring(0, s.length() - 2)
				+ "." + s.substring(s.length() - 2)));
	}

	public static String Cent2DollarShort(String s)
	{
		String ss = Cent2Dollar(s);
		ss = "" + Double.parseDouble(ss);
		if (ss.endsWith(".0"))
			return ss.substring(0, ss.length() - 2);
		if (ss.endsWith(".00"))
			return ss.substring(0, ss.length() - 3);
		return ss;
	}

	public static String trim(String s)
	{
		if (s == null)
			return "";
		return s.trim();
	}

	public static String trimLeft(String chars, String s)
	{
		if (s == null)
			return "";
		if (s.length() <= 0)
			return s;
		if (chars == null)
			return s;
		if (chars.length() <= 0)
			return s;
		int i = 0;
		for (i = 0; i < s.length(); i++)
		{
			if (-1 == chars.indexOf(s.charAt(i)))
				break;
		}
		return s.substring(i);
	}

	public static String trimRight(String chars, String s)
	{
		if (s == null)
			return "";
		if (s.length() <= 0)
			return s;
		if (chars == null)
			return s;
		if (chars.length() <= 0)
			return s;
		int i = s.length();
		for (i = s.length() - 1; i > -1; i--)
		{
			if (-1 == chars.indexOf(s.charAt(i)))
				break;
		}
		if (i < 0)
			return "";
		return s.substring(0, i + 1);
	}

	public static String trimAll(String chars, String s)
	{
		if (s == null)
			return "";
		if (s.length() <= 0)
			return s;
		if (chars == null)
			return s;
		if (chars.length() <= 0)
			return s;
		int i = 0;
		int j = 0;
		StringBuffer sb = new StringBuffer(s);
		do
		{
			j = sb.length();
			for (i = 0; i < sb.length(); i++)
			{
				if (chars.indexOf(sb.charAt(i)) != -1)
					sb.replace(i, i + 1, "");
			}
		}
		while (j != sb.length());
		return sb.toString();
	}

	public static String compressSpace(String s)
	{
		if (s == null)
			return "";
		String ss = s.trim();
		String tmp = "";
		boolean lastIsSpace = false;
		for (int i = 0; i < ss.length(); i++)
		{
			if (ss.charAt(i) != ' ' && ss.charAt(i) != '\t')
			{
				tmp += ss.charAt(i);
				lastIsSpace = false;
			}
			else if (!lastIsSpace)
			{
				tmp += ' ';
				lastIsSpace = true;
			}
		}
		return tmp;
	}

	public static boolean isDigitalString(String s)
	{
		if (s == null)
			return false;
		if (s.length() == 0)
			return false;
		String ds = "0123456789";
		for (int i = 0; i < s.length(); i++)
		{
			if (ds.indexOf(s.charAt(i)) < 0)
				return false;
		}
		return true;
	}

	public static boolean isLetterString(String s)
	{
		if (s == null)
			return false;
		if (s.length() == 0)
			return false;
		String ds = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		for (int i = 0; i < s.length(); i++)
		{
			if (ds.indexOf(s.charAt(i)) < 0)
				return false;
		}
		return true;
	}

	public static String ISO8859toGB2312(String s)
	{
		try
		{
			return new String(s.getBytes("ISO-8859-1"), "GB2312");
		}
		catch (UnsupportedEncodingException e)
		{
			return null;
		}
	}

	public static String DBCharset2HostCharset(String s)
	{
		try
		{
			return new String(s.getBytes("ISO-8859-1"), "GBK");
		}
		catch (UnsupportedEncodingException e)
		{
			return null;
		}
	}

	public static String LeftPaddingChar(char c, int l, String string)
	{
		String str = "";
		String cs = "";
		if (string.length() > l)
			str = string;
		else
			for (int i = 0; i < l - string.length(); i++)
				cs = cs + c;
		str = cs + string;
		return str;
	}

	public static String RightPaddingChar(char c, int l, String string)
	{
		String str = "";
		String cs = "";
		if (string.length() > l)
			str = string;
		else
			for (int i = 0; i < l - string.length(); i++)
				cs = cs + c;
		str = string + cs;
		return str;
	}

	/**
	 * �ַ�������
	 * 
	 * @param password
	 *            �ַ�����
	 * @param algorithm
	 *            �����㷨��SHA �� MD5
	 * @return
	 */
	public static String encodePassword(String password, String algorithm)
	{
		byte[] unencodedPassword = password.getBytes();
		MessageDigest md = null;
		try
		{
			md = MessageDigest.getInstance(algorithm);
		}
		catch (Exception e)
		{

			return password;
		}
		md.reset();
		md.update(unencodedPassword);
		byte[] encodedPassword = md.digest();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < encodedPassword.length; i++)
		{
			if ((encodedPassword[i] & 0xff) < 0x10)
			{
				buf.append("0");
			}
			buf.append(Long.toString(encodedPassword[i] & 0xff, 16));
		}
		return buf.toString();
	}

	/**
	 * MD5�����ַ�
	 * 
	 * @param str
	 * @return
	 */
	public static String encodeMD5(String str)
	{
		return encodePassword(str, "MD5");
	}

	/**
	 * UCS2����
	 * 
	 * @param src
	 *            UCS2 Դ��
	 * @return ������UTF-16BE�ַ�
	 * @throws Exception
	 */
	public static String DecodeUCS2(String src) throws Exception
	{
		byte[] bytes = new byte[src.length() / 2];
		for (int i = 0; i < src.length(); i += 2)
		{
			bytes[i / 2] = (byte) (Integer.parseInt(src.substring(i, i + 2), 16));
		}
		String reValue;
		try
		{
			reValue = new String(bytes, "UTF-16BE");
		}
		catch (UnsupportedEncodingException e)
		{
			throw new Exception(e);
		}
		return reValue;

	}

	/**
	 * UCS2����
	 * 
	 * @param src
	 *            UTF-16BE�����Դ��
	 * @return ������UCS2��
	 * @throws Exception
	 */
	public static String EncodeUCS2(String src) throws Exception
	{

		byte[] bytes;
		try
		{
			bytes = src.getBytes("UTF-16BE");
		}
		catch (UnsupportedEncodingException e)
		{
			throw new Exception(e);
		}

		StringBuffer reValue = new StringBuffer();
		StringBuffer tem = new StringBuffer();
		for (int i = 0; i < bytes.length; i++)
		{
			tem.delete(0, tem.length());
			tem.append(Integer.toHexString(bytes[i] & 0xFF));
			if (tem.length() == 1)
			{
				tem.insert(0, '0');
			}
			reValue.append(tem);
		}
		return reValue.toString().toUpperCase();
	}

	public static String getUCS24Ecard_hanzi(String hanzi) throws Exception
	{
		// ����+DCS(Ӣ��04;����08)+UCS2����+��λ'F'
		String ucs2 = EncodeUCS2(hanzi);
		int len = ucs2.length() / 2;
		String len_hex = Integer.toHexString(len + 1).toUpperCase();
		if (len_hex.length() < 2)
			len_hex = "0" + len_hex;
		ucs2 = len_hex + "08" + ucs2;
		String padded = RightPaddingChar('F', 32, ucs2);
		return padded;
	}

	public static String getUCS24Ecard_english(String english) throws Exception
	{
		// ����+DCS(Ӣ��04;����08)+UCS2����+��λ'F'
		String ucs2 = EncodeUCS2(english);
		int len = ucs2.length() / 2;
		String len_hex = Integer.toHexString(len + 1).toUpperCase();
		if (len_hex.length() < 2)
			len_hex = "0" + len_hex;
		ucs2 = len_hex + "04" + ucs2;
		String padded = RightPaddingChar('F', 32, ucs2);
		return padded;
	}

	public static String getSourceCodeFromUCS24(String ucs2) throws Exception
	{
		String len_string = ucs2.substring(0, 2);
		int len = Integer.parseInt(len_string, 16);
		String ucs2Data_string = ucs2.substring(4, len * 2 + 2);
		String sourceCode = DecodeUCS2(ucs2Data_string);
		return sourceCode;
	}

	public static String rightAlign(String value, int length)
	{

		if (value == null) {
			return "";
		}
		int len = value.length();

		for (int j = 0; j < (length - len); j++)
		{
			value = "0" + value;
		}

		return value;
	}

	public String toAsc(String bcd_str)
	{
		int cnt;
		int conv_len = bcd_str.length();
		char ascii_buf[] = new char[2 * conv_len];
		char bcd_buf[] = bcd_str.toCharArray();
		String ss = "";

		for (cnt = 0; cnt < conv_len; cnt++)
		{
			ascii_buf[2 * cnt] = (char) ((bcd_buf[cnt] >> 4));
			ascii_buf[2 * cnt] += ((ascii_buf[2 * cnt] > 9) ? ('A' - 10) : '0');
			ascii_buf[2 * cnt + 1] = (char) ((bcd_buf[cnt] & 0x0f));
			ascii_buf[2 * cnt + 1] += ((ascii_buf[2 * cnt + 1] > 9) ? ('A' - 10) : '0');
		}
		for (int i = 0; i < 2 * conv_len; i++)
			ss += ascii_buf[i];
		return ss;
	}

	public byte[] AscToBcd(String a)
	{
		int t0, t1, k = 0;
		byte[] b = new byte[(a.length() + 1) / 2];
		byte[] c = a.toUpperCase().getBytes();
		for (int i = 0; i < c.length; i += 2)
		{
			if (i + 1 >= c.length)
			{
				t0 = c[c.length - 1];
				if ((t0 & 0x40) != 0)
					t0 = t0 + 0x09;
				b[k] = (byte) ((t0 << 4) & 0xf0);
				break;
			}
			t0 = c[i];
			t1 = c[i + 1];
			if ((t0 & 0x40) != 0)
				t0 = t0 + 0x09;
			if ((t1 & 0x40) != 0)
				t1 = t1 + 0x09;
			b[k++] = (byte) (((t0 << 4) & 0xf0) | (t1 & 0x0f));
		}
		return b;
	}

	public static void main(String[] args)

	{

		try
		{
			System.out.println(LeftPaddingChar('0', 8, "8"));
			// getSourceCodeFromUCS24("");
			// ��ҵһ��ͨ��,Ҫ�ӿ�Ƭ��ʾ��ҵ���,��Ҫ������ת��ΪUCS2����
			// ����+DCS(Ӣ��04;����08)+UCS2����+��λ'F'
			// ��������:
			// 0908805452A84F1852BFFFFFFFFFFFFF
			// ���������Ž�:
			// 0D08805452A84F1852BF95E87981FFFF
			// ��������Ǯ��:
			// 0D08805452A84F1852BF94B15305FFFF
			// �������ƿ���:
			// 0D08805452A84F1852BF800352E4FFFF
			// �����������:
			// 0D08805452A88054673A6D888D39FFFF
			// ������������Ǯ��
			// 805452A84F1852BF8054673A94B15305
			// ���������ѻ�Ǯ��
			// 805452A84F1852BF8131673A94B15305
			// �����ѻ�Ǯ��:
			// 0D08805452A88131673A94B15305FFFF
			// �����������:
			// 0D08805452A88054673A6D888D39FFFF
			// String ucs2 = EncodeUCS2("����Ǯ��");
			// System.out.println(ucs2);
			// ucs2 =
			// "4F01003100310031003100310031";
			// String normal = DecodeUCS2(ucs2);
			// System.out.println(normal);
			//
			// System.out.println("-==" +
			// getUCS24Ecard_hanzi("��������Ǯ��"));
			// System.out.println("-==" +
			// getSourceCodeFromUCS24(getUCS24Ecard_hanzi("��������Ǯ��")));
			//
			// // String tmp="C3BB5E5A52A4";
			// //
			// System.out.println("uu="+DecodeUCS2(tbSstem.out.print(rightAlign(1,8));

			// System.out.println(rightAlign("1",8));

		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}