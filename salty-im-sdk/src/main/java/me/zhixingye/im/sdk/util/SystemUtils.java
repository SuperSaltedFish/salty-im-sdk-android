package me.zhixingye.im.sdk.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Process;

import java.util.List;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class SystemUtils {

    public static String getCurrentProcessName(Context cxt) {
        if (VERSION.SDK_INT >= VERSION_CODES.P) {
            return Application.getProcessName();
        } else {
            ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
            if (am == null) {
                return null;
            }
            List<RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
            if (runningApps == null) {
                return null;
            }
            for (RunningAppProcessInfo info : runningApps) {
                if (info.pid == Process.myPid()) {
                    return info.processName;
                }
            }
        }
        return null;
    }
}
