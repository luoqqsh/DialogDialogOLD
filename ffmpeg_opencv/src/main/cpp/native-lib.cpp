#include <jni.h>
#include <string>
#include <android/log.h>

//c++执行.c方法
extern "C" {

}
extern "C" JNIEXPORT jstring JNICALL
Java_com_example_ffmpeg_1opencv_testUtil_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";

    //打印日志
    __android_log_print(ANDROID_LOG_DEBUG, "KevinVise", "get:a");

    return env->NewStringUTF(hello.c_str());

}
