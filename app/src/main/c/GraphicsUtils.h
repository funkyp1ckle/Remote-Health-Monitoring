#include <jni.h>

#ifndef _Included_GraphicsUtils
#define _Included_GraphicsUtils
#ifdef __cplusplus
extern "C" {
    #endif
    JNIEXPORT jobject JNICALL Java_com_pramit_rhm_ui_GraphicsUtils_blur(JNIEnv*, jclass, jobject, jint);
    JNIEXPORT jobject JNICALL Java_com_pramit_rhm_ui_GraphicsUtils_merge(JNIEnv*, jclass, jobject, jobject, jint, jint);
    int getBitmapWidth(JNIEnv*, jclass, jobject);
    int getBitmapHeight(JNIEnv*, jclass, jobject);
    int* getPixels(JNIEnv*, jobject);
    int blendPixels(int, int);
    #ifdef __cplusplus
}
#endif
#endif
