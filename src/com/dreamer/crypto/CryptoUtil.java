/***
 * author   : danjianjun
 * data     : 2016-04-22
 * version  : 1.0.0
 */

package com.dreamer.crypto;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import android.text.TextUtils;


/**
 * Created by 80135254 on 2016-04-15.
 */
public class CryptoUtil {
	/**
	 * 
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	/*
	 * public static byte[] decryptBASE64(String key) throws IOException {
	 * return (new BASE64Decoder()).decodeBuffer(key); }
	 */

	/**
	 * BASE64
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	/*
	 * public static String encryptBASE64(byte[] key) { return (new
	 * BASE64Encoder()).encodeBuffer(key); }
	 */

	/**
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	/***********************************************************************************/

	public static byte[] getUTF8Bytes(String string) {
		if (string == null)
			return new byte[0];

		try {
			return string.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {

			try {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				DataOutputStream dos = new DataOutputStream(bos);
				dos.writeUTF(string);
				byte[] jdata = bos.toByteArray();
				bos.close();
				dos.close();
				byte[] buff = new byte[jdata.length - 2];
				System.arraycopy(jdata, 2, buff, 0, buff.length);
				return buff;
			} catch (IOException ex) {
				return new byte[0];
			}
		}
	}

	public static byte[] swapBytes(byte[] b) {
		int length = b.length % 2 == 0 ? b.length : b.length - 1;
		for (int i = 0; i < length; i += 2) {
			byte tmp = b[i];
			b[i] = b[i + 1];
			b[i + 1] = tmp;
		}

		return b;
	}

	private static String getDesKey() {
		String desKey = "";
		byte[] keyByte = getUTF8Bytes(Constants.DES_KEY);
		keyByte = swapBytes(keyByte);
		if (null != keyByte) {
			desKey = new String(keyByte, Charset.forName("UTF-8"));
		}
		return desKey;
	}

	public static String desEncrypt(String data) {

		String encryptData = "";
		if (!TextUtils.isEmpty(data)) {
			try {
				encryptData = DESUtil.encrypt(data, getDesKey());
			} catch (Exception e) {
			}
		}
		return encryptData;

	}

	public static String desDecrypt(String data) {

		String decryptData = "";
		if (!TextUtils.isEmpty(data)) {
			try {
				decryptData = DESUtil.decrypt(data, getDesKey());
			} catch (Exception e) {
			}
		}
		return decryptData;

	}

}
