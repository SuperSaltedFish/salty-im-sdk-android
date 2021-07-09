package me.zhixingye.im.sdk.tool;

import android.os.Handler;
import android.os.Looper;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年07月09日.
 */
public class HandlerFactory {
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    public static Handler getMainHandler() {
        return MAIN_HANDLER;
    }
}
