/*
 * 20-1-13 下午3:14 coded form Zhonghua.
 */

package vip.inode.demo.webrtc;

/**
 * @author Zhonghua
 */
public class AutomaticGainControlUtils {
    static {
        System.loadLibrary("legacy_agc-lib");
    }

    public native long agcCreate();

    public native int agcFree(long agcInst);

    public native int agcInit(long agcInst, int minLevel, int maxLevel, int agcMode, int fs);

    public native int agcSetConfig(long agcInst, short targetLevelDbfs, short compressionGaindB, boolean limiterEnable);

    public native int agcProcess(long agcInst, short[] inNear, int num_bands, int samples, short[] out,
                                 int inMicLevel, int outMicLevel, int echo, boolean saturationWarning);

    public native int agcAddFarend(long agcInst, short[] inFar, int samples);

    public native int agcAddMic(long agcInst, short[] inMic, int num_bands, int samples);

    public native int agcVirtualMic(long agcInst, short[] inMic, int num_bands, int samples, int micLevelIn, int micLevelOut);
}
