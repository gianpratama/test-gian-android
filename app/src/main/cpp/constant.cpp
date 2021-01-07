#include <jni.h>

// Mock
extern "C"
JNIEXPORT jstring JNICALL
Java_com_sehatq_test_util_JNIUtil_getWebUrlImpl(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF("http://www.google.com/");
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_sehatq_test_util_JNIUtil_getApiUrlImpl(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF("https://private-4639ce-ecommerce56.apiary-mock.com/");
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_sehatq_test_util_JNIUtil_getApiKeyImpl(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF("f55e264564ec4550b45596d533ab66ca");
}