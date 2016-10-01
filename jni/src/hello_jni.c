#include <string.h>
#include <jni.h>

jstring Java_com_dreamer_example_jni_HelloJni_stringFromJNI(JNIEnv* env,
		jobject thiz) {
	return (*env)->NewStringUTF(env, "Hello World from JNI !");
}
