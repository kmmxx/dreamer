package com.dreamer.crypto;

import java.security.Key;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import android.util.Base64;

/**
 * <p>Title: DESUtil</p>
 * <p>Description: DESUtil</p>
 * <p>Copyright (c) 2014 www.oppo.com Inc. All rights reserved.</p>
 * <p>Company: OPPO</p>
 *
 * @author  Rebore
 * @version 1.0
 *
 */

public abstract class DESUtil {
    
    public static String encrypt(String data, String key) throws Exception {
        SecureRandom sr = new SecureRandom();
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, getDesKey(key), sr);
        byte[] plaintext = data.getBytes("UTF-8");
        byte[] output = cipher.doFinal(plaintext);
        String s = new String(Base64.encode(output, Base64.DEFAULT));
        return s.trim();
    }

    private static Key getDesKey(String keyStr) throws Exception {
        DESKeySpec desKeySpec = new DESKeySpec(Base64.decode(keyStr,
                Base64.DEFAULT));
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);
        return secretKey;
    }

    public static String decrypt(String data, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, getDesKey(key));
        byte[] plaintext = Base64.decode(data, Base64.DEFAULT);
        byte[] output = cipher.doFinal(plaintext);
        String s = new String(output);
        s = s.trim();
        return s;
    }

}


