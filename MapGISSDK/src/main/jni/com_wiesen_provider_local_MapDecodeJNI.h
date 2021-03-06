/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_wiesen_provider_local_MapDecodeJNI */

#ifndef _Included_com_wiesen_provider_local_MapDecodeJNI
#define _Included_com_wiesen_provider_local_MapDecodeJNI
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_wiesen_provider_local_MapDecodeJNI
 * Method:    getTileIndex
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_wiesen_provider_local_MapDecodeJNI_getTileIndex
  (JNIEnv *, jobject, jstring);

/*
 * Class:     com_wiesen_provider_local_MapDecodeJNI
 * Method:    decodeTileInfo
 * Signature: (Ljava/lang/String;II)V
 */
JNIEXPORT void JNICALL Java_com_wiesen_provider_local_MapDecodeJNI_decodeTileInfo
  (JNIEnv *, jobject, jstring, jint, jint);

/*
 * Class:     com_wiesen_provider_local_MapDecodeJNI
 * Method:    getTileMap
 * Signature: (Ljava/lang/String;II)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_wiesen_provider_local_MapDecodeJNI_getTileMap
  (JNIEnv *, jobject, jstring, jint, jint);

#ifdef __cplusplus
}
#endif
#endif
