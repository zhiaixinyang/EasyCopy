/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com.gerenvip.filescaner.FileScannerJni */

#ifndef _Included_com_gerenvip_filescaner_FileScannerJni
#define _Included_com_gerenvip_filescaner_FileScannerJni
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com.gerenvip.filescaner.FileScannerJni
 * Method:    scanDirs
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT jobject JNICALL Java_com_gerenvip_filescaner_FileScannerJni_scanDirs
        (JNIEnv *, jclass, jstring, jboolean);
/*
 * Class:     com.gerenvip.filescaner.FileScannerJni
 * Method:    scanFiles
 * Signature: (Ljava/lang/String;)Ljava/util/ArrayList;
 */
JNIEXPORT jobject JNICALL Java_com_gerenvip_filescaner_FileScannerJni_scanFiles
        (JNIEnv *, jobject, jstring);


/*
 * Class:    com.gerenvip.filescaner.FileScannerJni
 * Method:    scanUpdateDirs
 * Signature: (Ljava/lang/String;)Ljava/util/ArrayList;
 */
JNIEXPORT jobject JNICALL Java_com_gerenvip_filescaner_FileScannerJni_scanUpdateDirs
        (JNIEnv *, jclass, jstring);

/*
 * Class:     com.gerenvip.filescaner.FileScannerJni
 * Method:    getFileLastModifiedTime
 * Signature: (Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL
Java_com_gerenvip_filescaner_FileScannerJni_getFileLastModifiedTime
        (JNIEnv *, jclass, jstring);


void doScannerDirs(JNIEnv *env, char *path);
void doScannerFiles(JNIEnv *env, char *path);
void doScannerUpdateDirs(JNIEnv *env, char *path);
void init(JNIEnv *pEnv);
void finish(JNIEnv *env);

#ifdef __cplusplus
}
#endif
#endif
