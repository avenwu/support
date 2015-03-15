package net.avenwu.support.util;

/**
 * Created by chaobin on 3/15/15.
 */
public class Device {
    /**
     * simple hack to avoid umeng auto update crash the app on devices not based on arm
     * eg: genymotion based on i686
     */
    public static boolean isARMBasedDevice() {
        final String arch = System.getProperty("os.arch");
        return arch.startsWith("arm") || arch.startsWith("ARM");
    }
}
