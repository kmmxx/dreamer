package com.dreamer.example.jni;

public class HelloJni {

	public native String stringFromJNI();

	static {
		System.loadLibrary("hello_jni");
	}
}
