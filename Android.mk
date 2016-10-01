LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := eng

# Only compile source java files in this apk.
LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_PACKAGE_NAME := Dreamer
LOCAL_CERTIFICATE := platform

LOCAL_JNI_SHARED_LIBRARIES := hello_jni
LOCAL_STATIC_JAVA_LIBRARIES := Dreamer

include $(BUILD_PACKAGE)
include $(call all-makefiles-under,$(LOCAL_PATH))