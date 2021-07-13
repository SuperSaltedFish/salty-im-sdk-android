package me.zhixingye.im.tool;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年07月13日.
 */
public class ExecutorHelper {

    private static volatile Executor sWorkExecutor;

    public static Executor getsWorkExecutor() {
        if (sWorkExecutor == null) {
            synchronized (ExecutorHelper.class) {
                if (sWorkExecutor == null) {
                    sWorkExecutor = new ThreadPoolExecutor(
                            2,
                            Runtime.getRuntime().availableProcessors() + 1,
                            30, TimeUnit.SECONDS,
                            new LinkedBlockingQueue<>());
                }
            }
        }
        return sWorkExecutor;
    }
}
