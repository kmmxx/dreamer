package com.dreamer.pattern;

import java.io.Serializable;

import android.util.Pair;

public class Generic<T extends Object & Comparable & Serializable> {

	public Generic() {

	}
	
//	public void GenerateCommon(Pair<? extends Object> o){
//		
//	}

	public <T> T GenerateClass(Class<T> cl) {
		try {
			return cl.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public <T> void GenericMethod(T t) {
	}

	public <T> T GenericMethod1(T t) {
		return t;
	}

	public <T extends Comparable> T GenericMethod2(T t) {
		return t;
	}

	public <T extends Object> T GenericMethod3(T t) {
		return t;
	}

	public <T extends Object & Comparable & Serializable> T GenericMethod4(T t) {
		return t;
	}

}
