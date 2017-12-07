#include <jni.h>

JNIEXPORT void JNICALL
Java_yjy_com_commonproject_utils_ImageUtils_test(JNIEnv *env, jclass type, jint i, jstring s_) {
    const char *s = (*env).functions->GetStringUTFChars(env, s_, 0);

    // TODO

    (*env).functions->ReleaseStringUTFChars(env, s_, s);
}

JNIEXPORT void JNICALL
Java_yjy_com_commonproject_utils_ImageUtils_comproess(JNIEnv *env, jclass type, jobject context,
                                                      jobject bitmap, jint quality,
                                                      jstring fileNameStr_) {
    const char *fileNameStr = (*env).functions->GetStringUTFChars(env,fileNameStr_, 0);

    // TODO

    (*env).functions->ReleaseStringUTFChars(env,fileNameStr_, fileNameStr);
}