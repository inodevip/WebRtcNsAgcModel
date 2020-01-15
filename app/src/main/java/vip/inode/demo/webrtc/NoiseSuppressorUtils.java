/*
 * 20-1-13 下午3:12 coded form Zhonghua.
 */

package vip.inode.demo.webrtc;

/**
 * @author Zhonghua
 */
public class NoiseSuppressorUtils {
    static {
        System.loadLibrary("legacy_ns-lib");
    }

    public native long nsCreate();

    public native int nsInit(long nsHandler, int frequency);

    /**
     * @param mode 0: Mild, 1: Medium , 2: Aggressive
     * @return 0 - Ok
     * -1 - Error
     */
    public native int nsSetPolicy(long nsHandler, int mode);

    public native int nsProcess(long nsHandler, float[] spframe, int num_bands, float[] outframe);

    public native int nsFree(long nsHandler);

    public native long nsxCreate();

    public native int nsxInit(long nsxHandler, int frequency);

    /**
     * @param mode 0: Mild, 1: Medium , 2: Aggressive
     * @return 0 - Ok
     * -1 - Error
     */
    public native int nsxSetPolicy(long nsxHandler, int mode);

    public native int nsxProcess(long nsxHandler, short[] speechFrame, int num_bands, short[] outframe);

    public native int nsxFree(long nsxHandler);
}
