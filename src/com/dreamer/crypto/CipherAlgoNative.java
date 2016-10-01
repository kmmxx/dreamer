/***
 * author   : danjianjun
 * data     : 2016-04-22
 * version  : 1.0.0
 */


package com.dreamer.crypto;

import android.content.Context;


public class CipherAlgoNative {
    static {
        // The runtime will add "lib" on the front and ".o" on the end of
        // the name supplied to loadLibrary.
        System.loadLibrary("coloros_mcs_cipheralgo");
    }

    static native  byte[] nativeGenerateAESKey(Context context);
    //static native  byte[] nativeGenerateAESIV();
    static native  byte[] nativeGetApiKey();
    static native  byte[] nativeGetApiSecret();
    static native  byte[] nativeAESEncrypt(byte[] in, int len);
    static native  byte[] nativeAESDecrypt(byte[] array,int len);
}
