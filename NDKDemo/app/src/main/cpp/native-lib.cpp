#include <jni.h>
#include <string>
#include <math.h>
#include <android/log.h>
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,"called",__VA_ARGS__)
extern "C" JNIEXPORT jstring

JNICALL
Java_com_example_urvish_ndkdemo_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++ using Android NDK and JNI";
    return env->NewStringUTF(hello.c_str());
}
extern "C" JNIEXPORT float
JNICALL
Java_com_example_urvish_ndkdemo_MainActivity_computeGforce(JNIEnv *env, jobject) {
    float m1 = 1 * pow(10, 15);
    float m2 = 1 * pow(10, 5);
    float distance = 1 * pow(10, 8);
    const float gravitationalConst = 6.67408 * pow(10, -11);
    float ans;
    ans = (gravitationalConst*m1*m2)/pow(distance,2.0f);

    return ans;


}
extern "C" JNIEXPORT void
JNICALL
Java_com_example_urvish_ndkdemo_MainActivity_callback(JNIEnv *env,jobject obj,jint dept){
    jclass classObj=(* env).GetObjectClass(obj);
    jmethodID methidid=(* env).GetMethodID(classObj,"callBack","(I)V");
    if(methidid==0)
        return;
    printf("in c++,called=%d",dept);
    LOGD("in c++,called=%d",dept);
    (* env).CallVoidMethod(obj,methidid);

}