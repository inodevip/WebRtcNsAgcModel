#include <jni.h>
#include <string>
#include <cstdlib>

#include "modules/audio_processing/legacy_ns/noise_suppression.h"
#include "modules/audio_processing/legacy_ns/noise_suppression_x.h"

#if defined(__cplusplus)
extern "C" {
#endif


JNIEXPORT jlong JNICALL
Java_vip_inode_demo_webrtc_NoiseSuppressorUtils_nsCreate(JNIEnv *env, jobject obj) {
    return (long) WebRtcNs_Create();
}


JNIEXPORT jint JNICALL
Java_vip_inode_demo_webrtc_NoiseSuppressorUtils_nsInit(JNIEnv *env, jobject obj, jlong nsHandler,
                                                       jint frequency) {
    NsHandle *handler = (NsHandle *) nsHandler;
    if (handler == nullptr) {
        return -3;
    }
    return WebRtcNs_Init(handler, frequency);
}


JNIEXPORT jint JNICALL
Java_vip_inode_demo_webrtc_NoiseSuppressorUtils_nsSetPolicy(JNIEnv *env, jobject obj,
                                                            jlong nsHandler, jint mode) {
    NsHandle *handle = (NsHandle *) nsHandler;
    if (handle == nullptr) {
        return -3;
    }
    return WebRtcNs_set_policy(handle, mode);
}


JNIEXPORT jint JNICALL
Java_vip_inode_demo_webrtc_NoiseSuppressorUtils_nsProcess(JNIEnv *env,
                                                          jobject obj, jlong nsHandler,
                                                          jfloatArray spframe, jint num_bands,
                                                          jfloatArray outframe) {
    NsHandle *handle = (NsHandle *) nsHandler;
    if (handle == nullptr) {
        return -3;
    }
    jfloat *cspframe = env->GetFloatArrayElements(spframe, nullptr);
    jfloat *coutframe = env->GetFloatArrayElements(outframe, nullptr);
    WebRtcNs_Process(handle, &cspframe, num_bands, &coutframe);
    env->ReleaseFloatArrayElements(spframe, cspframe, 0);
    env->ReleaseFloatArrayElements(outframe, coutframe, 0);
    return 0;
}

JNIEXPORT jint JNICALL
Java_vip_inode_demo_webrtc_NoiseSuppressorUtils_nsFree(JNIEnv *env,
                                                       jobject obj, jlong
                                                       nsHandler) {
    NsHandle *handle = (NsHandle *) nsHandler;
    if (handle == nullptr) {
        return -3;
    }
    WebRtcNs_Free(handle);
    return 0;
}

JNIEXPORT jlong JNICALL
Java_vip_inode_demo_webrtc_NoiseSuppressorUtils_nsxCreate(JNIEnv *env, jobject obj) {
    return (long) WebRtcNsx_Create();
}


JNIEXPORT jint JNICALL
Java_vip_inode_demo_webrtc_NoiseSuppressorUtils_nsxInit(JNIEnv *env, jobject obj, jlong nsHandler,
                                                        jint frequency
) {
    NsxHandle *handler = (NsxHandle *) nsHandler;
    if (handler == nullptr) {
        return -3;
    }
    return WebRtcNsx_Init(handler, frequency);
}


JNIEXPORT jint JNICALL
Java_vip_inode_demo_webrtc_NoiseSuppressorUtils_nsxSetPolicy(JNIEnv *env,
                                                             jobject obj, jlong
                                                             nsHandler,
                                                             jint mode
) {
    NsxHandle *handle = (NsxHandle *) nsHandler;
    if (handle == nullptr) {
        return -3;
    }
    return WebRtcNsx_set_policy(handle, mode);
}


JNIEXPORT jint JNICALL
Java_vip_inode_demo_webrtc_NoiseSuppressorUtils_nsxProcess(JNIEnv *env,
                                                           jobject obj, jlong
                                                           nsHandler,
                                                           jshortArray speechFrame,
                                                           jint num_bands,
                                                           jshortArray outframe) {
    NsxHandle *handle = (NsxHandle *) nsHandler;
    if (handle == nullptr) {
        return -3;
    }
    jshort *cspeechFrame = env->GetShortArrayElements(speechFrame, nullptr);
    jshort *coutframe = env->GetShortArrayElements(outframe, nullptr);
    WebRtcNsx_Process(handle, &cspeechFrame, num_bands, &coutframe);
    env->ReleaseShortArrayElements(speechFrame, cspeechFrame, 0);
    env->ReleaseShortArrayElements(outframe, coutframe, 0);
    return 0;
}

JNIEXPORT jint JNICALL
Java_vip_inode_demo_webrtc_NoiseSuppressorUtils_nsxFree(JNIEnv *env, jobject obj, jlong nsHandler) {
    NsxHandle *handle = (NsxHandle *) nsHandler;
    if (handle == nullptr) {
        return -3;
    }
    WebRtcNsx_Free(handle);
    return 0;
}

#if defined(__cplusplus)
}
#endif

