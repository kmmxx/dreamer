/***
 * author   : danjianjun
 * data     : 2016-04-22
 * version  : 1.0.0
 */
package com.dreamer.crypto;

import java.io.IOException;

import android.content.Context;

public class McsCipher {


    public static String encrypt(String plaintext) throws IOException {

        byte[] plain = plaintext.getBytes();
        byte[] cipher = CipherAlgoNative.nativeAESEncrypt(plain, plain.length);
        return CryptoUtil.parseByte2HexStr(cipher);
    }

    public static byte[] decrypt(String ciphertext) throws IOException {

        byte[] cipher = CryptoUtil.parseHexStr2Byte(ciphertext);
        byte[] plain = CipherAlgoNative.nativeAESDecrypt(cipher, cipher.length);
        return plain;

    }

    public static byte[] decrypt(byte[] bytes) throws IOException {

        byte[] plain = CipherAlgoNative.nativeAESDecrypt(bytes, bytes.length);
        return plain;

    }

//    public static String getAESKey() {
//        byte[] key = CipherAlgoNative.nativeGenerateAESKey();
//        return CryptoUtils.parseByte2HexStr(key);
//    }

    public static String getAESKey(Context context) {
        byte[] key = CipherAlgoNative.nativeGenerateAESKey(context);
        return CryptoUtil.parseByte2HexStr(key);
    }

    public static String getApiKey() {
        byte[] key = CipherAlgoNative.nativeGetApiKey();
        return CryptoUtil.parseByte2HexStr(key);
    }


    public static String getApiSecret() {
        byte[] secret = CipherAlgoNative.nativeGetApiSecret();
        return CryptoUtil.parseByte2HexStr(secret);
    }

   /* public String getAESIv() {
        byte[] iv = CipherAlgoNative.nativeGenerateAESIV();
        return CryptoUtils.parseByte2HexStr(iv);
    }*/
}
