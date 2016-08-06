//
// Created by wiesen on 16-8-4.
//
#include "com_wiesen_provider_local_MapDecodeJNI.h"
#include <iostream>
#include <fstream>
#include "Log.h"

/*
 * Class:     com_wiesen_provider_local_MapDecodeJNI
 * Method:    getTileIndex
 * Signature: (Ljava/lang/String;)V;
 */
void JNICALL Java_com_wiesen_provider_local_MapDecodeJNI_getTileIndex
        (JNIEnv * env, jobject object, jstring path) {


    const char *j_path = env->GetStringUTFChars(path, 0);
    std::ifstream in(j_path,std::ios::in|std::ios::binary);
    char *index = new char[8 * 1024];
    if (!in) {
        MY_LOG_ERROR("file open error");
        return;
    }
    in.seekg(0, std::ios_base::beg);
    in.read(index, 8 * 1024);
    in.close();
    env->ReleaseStringUTFChars(path,j_path);

    jclass  clazz = env->GetObjectClass(object);
    if (clazz == NULL){
        MY_LOG_ERROR("clazz is null");
        return;
    }
    jmethodID  method_id = env->GetMethodID(clazz,"parseIndex","(Ljava/lang/String;)V");

    if (method_id == NULL){
        MY_LOG_ERROR("not found parseIndex(String) method");
        return;
    }
    jstring index_str = env->NewStringUTF(index);
    env->CallVoidMethod(object,method_id,index_str);
    env->ReleaseStringUTFChars(index_str,index);
    free(index);
}

void Java_com_wiesen_provider_local_MapDecodeJNI_decodeTileInfo(JNIEnv *env, jobject object,
                                                                jstring path, jint start,
                                                                jint length) {
    const char *j_path = env->GetStringUTFChars(path, 0);
    std::ifstream file(j_path,std::ios::in|std::ios::binary);
    char * tile_info = new char[length];
    if (!file){
        return;
    }

    file.seekg(start,std::ios_base::beg);
    file.read(tile_info,length);
    file.close();

    env->ReleaseStringUTFChars(path,j_path);


    jclass  clazz = env->GetObjectClass(object);
    if (clazz == NULL){
        MY_LOG_ERROR("clazz is null");
        return;
    }
    jmethodID  method_id = env->GetMethodID(clazz,"setTileInfo","([B)V");

    if (method_id == NULL){
        MY_LOG_ERROR("not found setTileInfo([B) method");
        return;
    }
    jbyteArray bytes = env->NewByteArray(length);
    env->SetByteArrayRegion(bytes, 0,length, (jbyte *) tile_info);
    env->CallVoidMethod(object,method_id,bytes);
    free(tile_info);
}


/*
 * Class:     com_wiesen_provider_local_MapDecodeJNI
 * Method:    getTileMap
 * Signature: (Ljava/lang/String;II)[B
 */
jbyteArray JNICALL Java_com_wiesen_provider_local_MapDecodeJNI_getTileMap
        (JNIEnv * env, jobject object, jstring path,jint start, jint length){
    const char *j_path = env->GetStringUTFChars(path, 0);
    std::ifstream file(j_path,std::ios::in|std::ios::binary);
    char * map_data = new char[length];
    if (!file){
        return NULL;
    }

    file.seekg(start,std::ios_base::beg);
    file.read(map_data,length);
    file.close();
    env->ReleaseStringUTFChars(path,j_path);

    jbyteArray bytes = env->NewByteArray(length);
    env->SetByteArrayRegion(bytes, 0, length, (jbyte *) map_data);
    free(map_data);
    return bytes;
}




