package com.dreamer.tool.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Tools {
    /**
     * 将字符串进行MD5加密
     * 
     * @param input 输入字符�?
     * @return
     */
    public static String calcMD5(String input) {
        return calcMD5(input.getBytes());
    }
    
    /**
     * MD5 加密
     * 
     * @param data
     * @return
     */
    public static String calcMD5(byte[] data) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return "";
        }

        // 生成MessageDigest
        md.update(data);
        byte[] hash = md.digest();

        // 转换为字符串
        StringBuffer sbRet = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            int v = hash[i] & 0xFF;
            if (v < 16)
                sbRet.append("0");
            sbRet.append(Integer.toString(v, 16));
        }
        return sbRet.toString();
    }
    
    public static String encode(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] result = digest.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : result) {
                int number = b & 0xff ; 
                String hex = Integer.toHexString(number);
                if (hex.length() == 1) {
                    sb.append("0");
                }
                sb.append(hex);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
            // can't reach
        }
    }
}
