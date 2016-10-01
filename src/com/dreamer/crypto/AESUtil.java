package com.dreamer.crypto;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.text.TextUtils;

public class AESUtil {

    public static final String TAG = "AESUtils";
    public static final String PADDING = "AES/CBC/PKCS5Padding";
    public static final String AES = "AES";
    public static final String SHA1PRNG = "SHA1PRNG";
    public static final String CRYPTO = "Crypto";
    public static final String HEX = "0123456789ABCDEF";
    public static final String SEED_DEFAULT = "com.dreamer.crypt.seed.defalut";

    /**
     * 加密
     *
     * @param content
     *            需要加密的内容
     * @param seed
     *            加密种子
     * @return
     */
    public static String encrypt(String seed, String content) throws Exception {
        if (TextUtils.isEmpty(content))
            return null;
        byte[] rawKey = getRawKey(seed.getBytes());
        byte[] result = encrypt(rawKey, content.getBytes());
        return toHex(result);
    }

    public static String decrypt(String seed, String encrypted) throws Exception {
        if (TextUtils.isEmpty(encrypted))
            return null;
        byte[] rawKey = getRawKey(seed.getBytes());
        byte[] enc = toByte(encrypted);
        byte[] result = decrypt(rawKey, enc);
        return new String(result);
    }
    
    public static String encrypt(byte[] rawKey, String content) throws Exception {
        if (TextUtils.isEmpty(content))
            return null;
        byte[] result = encrypt(rawKey, content.getBytes());
        return toHex(result);
    }

    public static String decrypt(byte[] rawKey, String encrypted) throws Exception {
        if (TextUtils.isEmpty(encrypted))
            return null;
        byte[] enc = toByte(encrypted);
        byte[] result = decrypt(rawKey, enc);
        return new String(result);
    }

    public static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(AES);
        SecureRandom sr = SecureRandom.getInstance(SHA1PRNG, CRYPTO);
        sr.setSeed(seed);
        kgen.init(128, sr);
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }

    public static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
        Cipher cipher = Cipher.getInstance(PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    public static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
        Cipher cipher = Cipher.getInstance(PADDING);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static String toHex(String txt) {
        return toHex(txt.getBytes());
    }

    public static String fromHex(String hex) {
        return new String(toByte(hex));
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    public static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }
}
